package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel

@Composable
expect fun UserQRCodeDialog(viewModel: EinkaufszettelViewModel, close: () -> Unit)