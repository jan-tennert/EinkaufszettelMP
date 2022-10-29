package io.github.jan.einkaufszettel.common.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun dynamicColors(
    darkMode: Boolean,
    fallback: () -> ColorScheme
): ColorScheme {
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    return if(dynamic) {
        val context = LocalContext.current
        if(darkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else fallback()
}