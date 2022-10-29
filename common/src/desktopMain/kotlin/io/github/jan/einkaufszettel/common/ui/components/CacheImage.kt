package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

@Suppress("NewApi")
@Composable
actual fun CacheImage(fileName: String, modifier: Modifier, loadingFallback: @Composable (() -> Unit)?, produceImage: suspend () -> ByteArray) {
    val tmpDir = remember { Path.of(System.getProperty("java.io.tmpdir")) }
    val bitmap by produceState<ImageBitmap?>(null) {
        val path = tmpDir.resolve(fileName)
        value = if(path.exists()) {
            Image.makeFromEncoded(path.toFile().readBytes()).toComposeImageBitmap()
        } else {
            val bytes = produceImage()
            path.writeBytes(bytes)
            Image.makeFromEncoded(bytes).toComposeImageBitmap()
        }
    }
    if(bitmap == null) {
        loadingFallback?.invoke()
    } else {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = modifier)
    }
}