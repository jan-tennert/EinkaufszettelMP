package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel

@Composable
actual fun UserQRCodeDialog(
    viewModel: EinkaufszettelViewModel,
    close: () -> Unit
) {
    throw IllegalStateException("This dialog is not supported on desktop")
}