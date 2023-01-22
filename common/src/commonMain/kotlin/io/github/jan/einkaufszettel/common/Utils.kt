package io.github.jan.einkaufszettel.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.KeyEvent
import kotlinx.coroutines.CoroutineDispatcher

expect inline fun KeyEvent.handleEnter(onEnter: () -> Unit): Boolean

expect fun ByteArray.toComposeImage(): ImageBitmap

expect val ioDispatcher: CoroutineDispatcher

expect fun osProperty(key: String): String?