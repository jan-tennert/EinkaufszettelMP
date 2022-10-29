package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings

@Composable
expect fun Dialog(
    close: () -> Unit,
    title: String,
    darkMode: EinkaufszettelSettings.DarkMode,
    width: Int = 400,
    height: Int = 300,
    content: @Composable () -> Unit
)