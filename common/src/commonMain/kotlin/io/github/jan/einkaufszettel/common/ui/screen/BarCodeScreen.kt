package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel

@Composable
expect fun BarCodeScreen(viewModel: EinkaufszettelViewModel)