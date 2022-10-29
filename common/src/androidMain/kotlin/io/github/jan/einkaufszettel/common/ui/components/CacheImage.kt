package io.github.jan.einkaufszettel.common.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun CacheImage(
    fileName: String,
    modifier: Modifier,
    loadingFallback: @Composable (() -> Unit)?,
    produceImage: suspend () -> ByteArray
) {
    val cacheDir = LocalContext.current.cacheDir
    val bitmap by produceState<ImageBitmap?>(null) {
        val path = cacheDir.resolve(fileName)
        value = if(path.exists()) {
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inMutable = true
            val bytes = path.readBytes()
            val bmp: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            bmp.asImageBitmap()
        } else {
            val bytes = produceImage()
            path.writeBytes(bytes)
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inMutable = true
            val bmp: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            bmp.asImageBitmap()
        }
    }
    if(bitmap == null) {
        loadingFallback?.invoke()
    } else {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = modifier)
    }
}