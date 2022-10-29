package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings

@Composable
actual fun Dialog(
    close: () -> Unit,
    title: String,
    darkMode: EinkaufszettelSettings.DarkMode,
    width: Int,
    height: Int,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(close) {
        content()
    }
}