package io.github.jan.einkaufszettel.common.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun dynamicColors(
    darkMode: Boolean,
    fallback: () -> ColorScheme
): ColorScheme = fallback()