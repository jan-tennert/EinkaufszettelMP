package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

@Composable
actual fun CacheImage(
    fileName: String,
    modifier: Modifier,
    loadingFallback: @Composable (() -> Unit)?,
    produceImage: suspend () -> ByteArray
) {
    val bitmap by produceState<ImageBitmap?>(null) {
        val bytes = produceImage()
        value = Image.makeFromEncoded(bytes).toComposeImageBitmap()
    }
    if(bitmap == null) {
        loadingFallback?.invoke()
    } else {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = modifier)
    }
}