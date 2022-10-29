package io.github.jan.einkaufszettel.common.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandle(action: () -> Unit) {
    BackHandler(true) {
        action()
    }
}
