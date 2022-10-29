package io.github.jan.einkaufszettel.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import org.jetbrains.skia.Image

@OptIn(ExperimentalComposeUiApi::class)
actual fun KeyEvent.handleEnter(onEnter: () -> Unit) {
    if(key == Key.Enter) onEnter()
}

actual fun ByteArray.toComposeImage(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}