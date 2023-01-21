package io.github.jan.einkaufszettel.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.KeyEvent

expect inline fun KeyEvent.handleEnter(onEnter: () -> Unit): Boolean

expect fun ByteArray.toComposeImage(): ImageBitmap