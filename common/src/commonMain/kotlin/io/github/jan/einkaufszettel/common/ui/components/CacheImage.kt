@file:JvmName("CacheImageF")
package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.jan.supabase.storage.BucketApi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal val HTTP_CLIENT = HttpClient()

@Composable
expect fun CacheImage(fileName: String, modifier: Modifier = Modifier, loadingFallback: @Composable (() -> Unit)? = null, produceImage: suspend () -> ByteArray)

@Composable
fun CacheImage(url: String, modifier: Modifier = Modifier, loadingFallback: @Composable (() -> Unit)? = null) = CacheImage(url.substringAfterLast("/"), modifier, loadingFallback, produceImage = {
    HTTP_CLIENT.get(url).body()
})

@Composable
fun BucketApi.CacheImageAuthenticated(path: String, modifier: Modifier = Modifier, loadingFallback: @Composable (() -> Unit)? = null) = CacheImage(path.substringAfterLast("/"), modifier, loadingFallback) {
    downloadAuthenticated(path)
}

@Composable
fun BucketApi.CacheImagePublic(path: String, modifier: Modifier = Modifier, loadingFallback: @Composable (() -> Unit)? = null) = CacheImage(path.substringAfterLast("/"), modifier, loadingFallback) {
    downloadPublic(path)
}