package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size

@Composable
actual fun CacheImage(
    data: CacheData,
    modifier: Modifier,
    size: CacheSize,
    loadingFallback: @Composable (() -> Unit)?
) {
    val context = LocalContext.current
    val imageData = remember(data, size) {
        when(data) {
            is CacheData.Authenticated -> ImageRequest.Builder(context)
                .data(data.url)
                .addHeader("Authorization", "Bearer ${data.bearerToken}")
                .size(size.size)
                .build()
            is CacheData.Public -> ImageRequest.Builder(context)
                .data(data.url)
                .size(size.size)
                .build()
        }
    }
    var loading by remember { mutableStateOf(true) }
    Box {
        AsyncImage(
            model = imageData,
            modifier = modifier,
            contentDescription = null,
            onLoading = {
                loading = true
            },
            onSuccess = {
                loading = false
            }
        )
        if(loading) {
            loadingFallback?.invoke()
        }
    }
}

actual class CacheSize actual constructor(width: Int, height: Int) {

    val size = if(width == -1) Size.ORIGINAL else Size(width, height)

}