package io.github.jan.einkaufszettel.common

import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.github.aakira.napier.Napier
import io.github.jan.einkaufszettel.common.data.local.*
import io.github.jan.einkaufszettel.common.data.remote.*
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.realtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

expect open class AppViewModel() : InstanceKeeper.Instance {

    val scope: CoroutineScope

}

class EinkaufszettelViewModel(
    private val settings: EinkaufszettelSettings,
    private val profileApi: ProfileApi,
    private val productsApi: ProductEntryApi,
    private val shopApi: ShopApi,
    private val shopDataSource: ShopDataSource,
    private val productEntryDataSource: ProductEntryDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val realtimeChannel: RealtimeChannel,
    val clipboardManager: ClipboardManager,
    val supabaseClient: SupabaseClient
): AppViewModel() {

    val darkMode = settings.darkMode
    val sessionStatus = supabaseClient.gotrue.sessionStatus
    private val _profileStatus = MutableStateFlow<ProfileStatus>(ProfileStatus.NotTried)
    val profileStatus = _profileStatus.asStateFlow()
    val shopFlow = shopDataSource.getAllShops()
    val productEntryFlow = productEntryDataSource.getAllEntries()
    val localUserFlow = localUserDataSource.getUsers()
    val events = mutableStateListOf<UIEvent>()

    //realtime
    private var shopChangeFlowJob = MutableStateFlow<Job?>(null)
    private var productChangeFlowJob = MutableStateFlow<Job?>(null)

    init {
        scope.launch {
            settings.profile.collect {
                if(it != null) {
                    _profileStatus.value = ProfileStatus.Available(it)
                }
            }
        }
        scope.launch {
            productEntryFlow.collect {
                println(it)
            }
        }
    }

    fun connectToRealtime() {
        scope.launch {
            if(supabaseClient.realtime.status.value == Realtime.Status.CONNECTED) return@launch
            if(realtimeChannel.status.value == RealtimeChannel.Status.JOINED) return@launch
            supabaseClient.realtime.connect()
            productChangeFlowJob.value = realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
                table = "products"
            }.onEach {
                handleProductChange(it)
            }.launchIn(scope)
            shopChangeFlowJob.value = realtimeChannel.postgresChangeFlow<PostgresAction>("public") {
                table = "shops"
            }.onEach {
                handleShopChange(it)
            }.launchIn(scope)
            realtimeChannel.join()
        }
    }

    fun disconnectFromRealtime() {
        scope.launch {
            realtimeChannel.leave()
            supabaseClient.realtime.disconnect()
            productChangeFlowJob.value?.cancel()
            shopChangeFlowJob.value?.cancel()
        }
    }

    fun loginWithGoogle() {
        scope.launch {
            kotlin.runCatching {
                supabaseClient.gotrue.loginWith(Google)
            }.onSuccess {
                retrieveProfile()
            }
        }
    }

    fun loginWithEmail(config: Email.Config.() -> Unit) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                supabaseClient.gotrue.loginWith(Email, config = config)
                println("??????")
            }.onFailure {
                when(it) {
                    is RestException -> events.add(UIEvent.Alert("Login fehlgeschlagen. Bitte überprüfe deine Anmeldedaten."))
                    else -> events.add(UIEvent.Alert("Login fehlgeschlagen. Bitte überprüfe deine Internetverbindung"))
                }
            }.onSuccess {
                println("retrieving profile")
                retrieveProfile()
            }
        }
    }

    fun signUpWithEmail(config: Email.Config.() -> Unit) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                supabaseClient.gotrue.signUpWith(Email, config = config)
            }.onFailure {
                when(it) {
                    is RestException -> events.add(UIEvent.Alert("Registrierung fehlgeschlagen. Bitte überprüfe deine Anmeldedaten."))
                    else -> events.add(UIEvent.Alert("Registrierung fehlgeschlagen. Bitte überprüfe deine Internetverbindung"))
                }
            }
        }
    }

    fun sendPasswordRecovery(email: String) {
        scope.launch {
            kotlin.runCatching {
                supabaseClient.gotrue.sendRecoveryEmail(email)
            }.onSuccess {
                events.add(UIEvent.Alert("Eine E-Mail mit weiteren Anweisungen wurde an $email gesendet"))
            }.onFailure {
                when(it) {
                    is RestException -> events.add(UIEvent.Alert("E-Mail konnte nicht gesendet werden. Bitte überprüfe deine E-Mail-Adresse."))
                    else -> events.add(UIEvent.Alert("E-Mail konnte nicht gesendet werden. Bitte überprüfe deine Internetverbindung"))
                }
            }
        }
    }

    fun changePasswordTo(password: String) {
        scope.launch {
            kotlin.runCatching {
                supabaseClient.gotrue.modifyUser(Email) {
                    this.password = password
                }
            }.onSuccess {
                events.add(UIEvent.Alert("Passwort erfolgreich geändert."))
            }.onFailure {
                it.printStackTrace()
                events.add(UIEvent.Alert("Konnte Passwort nicht ändern. Bitte überprüfe deine Internetverbindung."))
            }
        }
    }

    fun retrieveProfile(load: Boolean = true) {
        scope.launch {
            if(load) _profileStatus.value = ProfileStatus.Loading
            kotlin.runCatching {
                profileApi.retrieveProfile(supabaseClient.gotrue.currentSessionOrNull()?.user?.id ?: throw IllegalStateException("Session shouldn't be null here"))
            }.onFailure {
                Napier.e(it) { "Failed to retrieve profile" }
                if(load) _profileStatus.value = ProfileStatus.NotExisting
            }.onSuccess {
                settings.setProfile(it)
                localUserDataSource.insertUser(it)
            }
        }
    }

    fun createProfile(username: String) {
        scope.launch {
            _profileStatus.value = ProfileStatus.Loading
            kotlin.runCatching {
                profileApi.createProfile(supabaseClient.gotrue.currentSessionOrNull()?.user?.id ?: throw IllegalStateException("Id shouldn't be null here"), username)
            }.onFailure {
                Napier.e(it) { "Failed to create profile" }
                _profileStatus.value = ProfileStatus.NotExisting
            }.onSuccess {
                settings.setProfile(it)
            }
        }
    }

    fun updateUsername(username: String) {
        val id = supabaseClient.gotrue.currentSessionOrNull()?.user?.id
        scope.launch {
            kotlin.runCatching {
                profileApi.updateProfile(id ?: throw IllegalStateException("Id shouldn't be null here"), username)
            }.onFailure {
                Napier.e(it) { "Failed to update username" }
                events.add(UIEvent.Alert("Fehler beim Aktualisieren des Benutzernamens"))
            }.onSuccess {
                settings.setProfile(RemoteUser(id!!, username))
                events.add(UIEvent.Alert("Benutzername erfolgreich aktualisiert"))
            }
        }
    }

    fun logout() {
        scope.launch(Dispatchers.IO) {
            supabaseClient.gotrue.invalidateSession()
            settings.setProfile(null)
        }
    }

    //actual data
    fun retrieveProducts() {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.retrieveProducts()
            }.onSuccess {
                productEntryDataSource.insertAll(it)
            }
        }
    }

    fun retrieveShops() {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                shopApi.retrieveShops()
            }.onSuccess {
                it.forEach { shops -> checkForNewUsers(shops) }
                shopDataSource.insertAll(it)
            }
        }
    }

    //profiles
    fun retrieveProfiles(ids: List<String>) {
        scope.launch {
            runCatching {
                profileApi.retrieveProfilesFromIds(ids)
            }.onSuccess {
                localUserDataSource.insertAll(it)
            }
        }
    }

    fun retrieveSingleProfile(id: String) {
        scope.launch {
            runCatching {
                profileApi.retrieveProfile(id)
            }.onSuccess {
                localUserDataSource.insertUser(it)
            }.onFailure {
                when(it) {
                    is RestException -> events.add(UIEvent.Alert("Fehler beim Laden des Benutzers. Bitte überprüfe die Id"))
                    else -> events.add(UIEvent.Alert("Fehler beim Laden des Benutzers. Bitte überprüfe deine Internetverbindung"))
                }
            }
        }
    }

    fun removeProfile(id: String) {
        scope.launch {
            kotlin.runCatching {
                localUserDataSource.deleteUserById(id)
            }
        }
    }

    private fun checkForNewUsers(shop: Shop) {
        scope.launch(Dispatchers.IO) {
            val currentCache = localUserDataSource.retrieveAllUsers().map { it.id }
            val newUsers = shop.authorizedUsers.filter { it !in currentCache  }
            if(newUsers.isNotEmpty()) {
                retrieveProfiles(newUsers)
            }
        }
    }

    //realtime
    private fun handleProductChange(action: PostgresAction) {
        println(action)
        scope.launch {
            when(action) {
                is PostgresAction.Delete -> productEntryDataSource.deleteEntryById(action.oldRecord["id"]?.jsonPrimitive?.longOrNull ?: throw IllegalStateException("Realtime delete without id (products)"))
                is PostgresAction.Insert -> productEntryDataSource.insertEntry(action.decodeRecordOrNull() ?: throw IllegalStateException("Couldn't decode new product record"))
                is PostgresAction.Update -> productEntryDataSource.insertEntry(action.decodeRecordOrNull() ?: throw IllegalStateException("Couldn't decode updated product record"))
                else -> {}
            }
        }
    }

    private fun handleShopChange(action: PostgresAction) {
        scope.launch {
            when(action) {
                is PostgresAction.Delete -> shopDataSource.deleteById(action.oldRecord["id"]?.jsonPrimitive?.longOrNull ?: throw IllegalStateException("Realtime delete without id (shops)"))
                is PostgresAction.Insert -> {
                    val shop = action.decodeRecordOrNull<Shop>() ?: throw IllegalStateException("Couldn't decode new shop record")
                    checkForNewUsers(shop)
                    shopDataSource.insertShop(action.decodeRecordOrNull<Shop>() ?: throw IllegalStateException("Couldn't decode new shop record"))
                }
                is PostgresAction.Update -> {
                    val shop = action.decodeRecordOrNull<Shop>() ?: throw IllegalStateException("Couldn't decode new shop record")
                    checkForNewUsers(shop)
                    shopDataSource.insertShop(action.decodeRecordOrNull<Shop>() ?: throw IllegalStateException("Couldn't decode new shop record"))
                }
                else -> {}
            }
        }
    }

    //actual shopping list
    fun markEntryAsDone(id: Long, callback: () -> Unit) {
        val ownUserId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id ?: throw IllegalStateException("Session shouldn't be null here")
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.markAsDone(id.toInt(), ownUserId)
            }.onSuccess {
                productEntryDataSource.markEntryAsDone(id, ownUserId)
                callback()
            }.onFailure {
                events.add(UIEvent.Alert("Fehler beim Markieren des Produkts als erledigt. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun markEntryAsNotDone(id: Long, callback: () -> Unit) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.markAsUndone(id.toInt())
            }.onSuccess {
                productEntryDataSource.markEntryUndone(id)
                callback()
            }.onFailure {
                events.add(UIEvent.Alert("Fehler beim Markieren des Produkts als nicht erledigt. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun deleteEntry(id: Long) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.deleteProduct(id.toInt())
            }.onSuccess {
                productEntryDataSource.deleteEntryById(id)
            }.onFailure {
                events.add(UIEvent.Alert("Fehler beim Löschen des Produkts. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun createEntry(shopId: Long, content: String) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.createProduct(shopId.toInt(), content, supabaseClient.gotrue.currentSessionOrNull()?.user?.id ?: throw IllegalStateException("Session shouldn't be null here"))
            }.onSuccess {
                productEntryDataSource.insertEntry(it)
            }.onFailure {
                events.add(UIEvent.Alert("Fehler beim Erstellen des Produkts. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun editEntry(id: Long, content: String) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                productsApi.editContent(id.toInt(), content)
            }.onSuccess {
                productEntryDataSource.editEntryContent(id, content)
            }.onFailure {
                Napier.e(it) { "Error while editing entry" }
                events.add(UIEvent.Alert("Fehler beim Bearbeiten des Produkts. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    //shop creation & modification
    fun createShop(name: String, fileInfo: FileInfo, authorizedUsers: List<String>) {
        val ownId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id ?: throw IllegalStateException("Session shouldn't be null here")
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                shopApi.createShop(name, fileInfo, ownId, authorizedUsers)
            }.onSuccess {
                shopDataSource.insertShop(it)
            }.onFailure {
                Napier.e(it) { "Error while creating shop" }
                events.add(UIEvent.Alert("Fehler beim Erstellen des Shops. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun editShop(id: Long, name: String, authorizedUsers: List<String>) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                shopApi.editShop(id.toInt(), name, authorizedUsers)
            }.onSuccess {
                shopDataSource.insertShop(it)
            }.onFailure {
                Napier.e(it) { "Error while editing shop" }
                events.add(UIEvent.Alert("Fehler beim Bearbeiten des Shops. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

    fun deleteShop(id: Long, iconUrl: String) {
        scope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                shopApi.deleteShop(id.toInt(), iconUrl)
            }.onSuccess {
                shopDataSource.deleteById(id)
            }.onFailure {
                Napier.e(it) { "Error while deleting shop" }
                events.add(UIEvent.Alert("Fehler beim Löschen des Shops. Bitte überprüfe deine Internetverbindung"))
            }
        }
    }

}