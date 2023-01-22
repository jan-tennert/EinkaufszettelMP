package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.runtime.Composable
import io.github.jan.einkaufszettel.common.data.remote.FileInfo

@Composable
actual fun ImageChooser(
    show: Boolean,
    onSelect: (info: FileInfo) -> Unit
) {
}