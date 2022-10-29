package io.github.jan.einkaufszettel.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.KeyEvent

expect fun KeyEvent.handleEnter(onEnter: () -> Unit)

expect fun ByteArray.toComposeImage(): ImageBitmap