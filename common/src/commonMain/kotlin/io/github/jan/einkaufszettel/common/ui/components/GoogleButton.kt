package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel

@Composable
expect fun GoogleButton(
modifier: Modifier,
text: String,
viewModel: EinkaufszettelViewModel
)