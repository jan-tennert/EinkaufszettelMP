package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

@Suppress("NewApi")
@Composable
fun CacheImage(fileName: String, modifier: Modifier, loadingFallback: @Composable (() -> Unit)?, produceImage: suspend () -> ByteArray) {

}

@Composable
actual fun CacheImage(
    data: CacheData,
    modifier: Modifier,
    size: CacheSize,
    loadingFallback: @Composable (() -> Unit)?
) {
    val tmpDir = remember { Path.of(System.getProperty("java.io.tmpdir")) }
    val bitmap by produceState<ImageBitmap?>(null) {
        withContext(Dispatchers.IO) {
            val fileName = data.url.substringAfterLast("/")
            val path = tmpDir.resolve(fileName)
            value = if(path.exists()) {
                Image.makeFromEncoded(path.toFile().readBytes()).toComposeImageBitmap()
            } else {
                val response = when(data) {
                    is CacheData.Authenticated -> HTTP_CLIENT.get(data.url) {
                        header("Authorization", "Bearer ${data.bearerToken}")
                    }
                    is CacheData.Public -> HTTP_CLIENT.get(data.url)
                }
                val bytes = response.body<ByteArray>()
                path.writeBytes(bytes)
                Image.makeFromEncoded(bytes).toComposeImageBitmap()
            }
        }
    }
    if(bitmap == null) {
        loadingFallback?.invoke()
    } else {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = modifier)
    }
}

actual class CacheSize actual constructor(width: Int, height: Int)