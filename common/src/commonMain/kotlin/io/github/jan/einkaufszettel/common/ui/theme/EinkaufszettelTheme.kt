package io.github.jan.einkaufszettel.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings

val MaterialTheme.topPadding get() = 10.dp

val DarkColorPalette = darkColorScheme(
   // background = Color(44, 47, 51)
)

val LightColorPalette = lightColorScheme()

@Composable
fun EinkaufszettelTheme(
    darkMode: EinkaufszettelSettings.DarkMode,
    content: @Composable () -> Unit
) {
    val isDarkMode = when(darkMode) {
        EinkaufszettelSettings.DarkMode.NOT_SET -> isSystemInDarkTheme()
        EinkaufszettelSettings.DarkMode.ON -> true
        EinkaufszettelSettings.DarkMode.OFF -> false
    }
    val colorScheme = dynamicColors(isDarkMode) {
        if (isDarkMode) DarkColorPalette else LightColorPalette
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
expect fun dynamicColors(darkMode: Boolean, fallback: () -> ColorScheme): ColorScheme