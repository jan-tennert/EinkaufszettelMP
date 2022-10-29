@file:OptIn(ExperimentalSettingsApi::class)
package io.github.jan.einkaufszettel.common.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.jan.einkaufszettel.common.data.remote.RemoteUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface EinkaufszettelSettings {

    val darkMode: Flow<DarkMode>
    val profile: Flow<RemoteUser?>

    suspend fun setDarkMode(darkMode: DarkMode)

    suspend fun setProfile(profile: RemoteUser?)

    enum class DarkMode {
        NOT_SET,
        ON,
        OFF
    }

    companion object {
        const val SETTINGS_DARK_MODE = "dark_mode"
        const val SETTINGS_PROFILE = "profile"
    }

}

internal class EinkaufszettelSettingsImpl(
    settings: ObservableSettings
): EinkaufszettelSettings {


    private val settings = settings.toFlowSettings(Dispatchers.IO)

    override val darkMode: Flow<EinkaufszettelSettings.DarkMode> = settings.getStringFlow(EinkaufszettelSettings.SETTINGS_DARK_MODE, EinkaufszettelSettings.DarkMode.NOT_SET.name)
        .map { EinkaufszettelSettings.DarkMode.valueOf(it) }
    override val profile: Flow<RemoteUser?> = settings.getStringFlow(EinkaufszettelSettings.SETTINGS_PROFILE, "")
        .map { if(it.isEmpty()) null else Json.decodeFromString(it) }

    override suspend fun setDarkMode(darkMode: EinkaufszettelSettings.DarkMode) {
        settings.putString(EinkaufszettelSettings.SETTINGS_DARK_MODE, darkMode.name)
    }

    override suspend fun setProfile(profile: RemoteUser?) {
        if(profile != null) {
            settings.putString(EinkaufszettelSettings.SETTINGS_PROFILE, Json.encodeToString(profile))
        } else {
            settings.remove(EinkaufszettelSettings.SETTINGS_PROFILE)
        }
    }

}