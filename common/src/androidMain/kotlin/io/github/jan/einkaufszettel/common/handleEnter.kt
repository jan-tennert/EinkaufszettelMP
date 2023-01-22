package io.github.jan.einkaufszettel.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.KeyEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual inline fun KeyEvent.handleEnter(onEnter: () -> Unit): Boolean {
    return false
}

actual fun ByteArray.toComposeImage(): ImageBitmap {
    val options: BitmapFactory.Options = BitmapFactory.Options()
    options.inMutable = true
    val bmp: Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size, options)
    return bmp.asImageBitmap()
}

actual val ioDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO