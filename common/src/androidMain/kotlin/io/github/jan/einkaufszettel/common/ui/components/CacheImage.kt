package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import io.github.jan.einkaufszettel.common.toComposeImage

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
            path.readBytes().toComposeImage()
        } else {
            val bytes = produceImage()
            path.writeBytes(bytes)
            bytes.toComposeImage()
        }
    }
    if(bitmap == null) {
        loadingFallback?.invoke()
    } else {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = modifier)
    }
}