package io.github.jan.einkaufszettel.common.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Shop(
    val id: Int,
    val name: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("icon_url")
    val iconUrl: String,
    @SerialName("owner_id")
    val ownerId: String,
    @SerialName("authorized_users")
    val authorizedUsers: List<String>
)

class FileInfo(
    val extension: String,
    val bytes: ByteArray
)

interface ShopApi {

    suspend fun retrieveShops(): List<Shop>

    suspend fun createShop(
        name: String,
        icon: FileInfo,
        ownerId: String,
        authorizedUsers: List<String>
    )

    suspend fun editShop(
        id: Int,
        newName: String,
        authorizedUsers: List<String>
    )

}

internal class ShopApiImpl(
    supabaseClient: SupabaseClient
) : ShopApi {

    private val table = supabaseClient.postgrest["shops"]
    private val bucket = supabaseClient.storage["icons"]

    override suspend fun retrieveShops(): List<Shop> {
        return table.select().decodeAs()
    }

    override suspend fun createShop(
        name: String,
        icon: FileInfo,
        ownerId: String,
        authorizedUsers: List<String>
    ) {
        val iconUrl = bucket.upload(Clock.System.now().toEpochMilliseconds().toString() + "." + icon.extension, icon.bytes)
        table.insert(buildJsonObject {
            put("name", name)
            put("icon_url", iconUrl)
            put("owner_id", ownerId)
            put("authorized_users", JsonArray(authorizedUsers.map { JsonPrimitive(it) }))
        })
    }

    override suspend fun editShop(id: Int, newName: String, authorizedUsers: List<String>) {
        table.update({
            Shop::name setTo newName
            set("authorized_users", JsonArray(authorizedUsers.map { JsonPrimitive(it)}))
        }) {
            Shop::id eq id
        }
    }

}