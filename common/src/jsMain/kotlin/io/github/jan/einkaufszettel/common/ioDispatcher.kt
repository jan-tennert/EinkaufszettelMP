package io.github.jan.einkaufszettel.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skia.Image

actual val ioDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default

actual fun ByteArray.toComposeImage(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

@OptIn(ExperimentalComposeUiApi::class)
actual inline fun KeyEvent.handleEnter(onEnter: () -> Unit): Boolean {
    return if(key == Key.Enter) {
        onEnter()
        true
    } else false
}

actual fun osProperty(key: String): String? {
    return ""
}