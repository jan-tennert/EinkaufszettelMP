package io.github.jan.einkaufszettel.common.ui.events

import androidx.compose.runtime.Composable

@Composable
actual fun AlertDialog(
    message: String,
    confirmButton: @Composable () -> Unit,
    close: () -> Unit
) {
}