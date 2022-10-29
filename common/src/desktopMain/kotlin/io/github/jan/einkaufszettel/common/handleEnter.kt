package io.github.jan.einkaufszettel.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key

@OptIn(ExperimentalComposeUiApi::class)
actual fun KeyEvent.handleEnter(onEnter: () -> Unit) {
    if(key == Key.Enter) onEnter()
}