package io.github.jan.einkaufszettel.common

import androidx.compose.runtime.compositionLocalOf
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings

data class ThemeSettings(
    val darkMode: EinkaufszettelSettings.DarkMode
)

val LocalThemeSettings = compositionLocalOf { ThemeSettings(EinkaufszettelSettings.DarkMode.NOT_SET) }
