@file:JvmName("CacheImageF")
package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient

internal val HTTP_CLIENT = HttpClient()

sealed interface CacheData {
    val url: String

    data class Authenticated(override val url: String, val bearerToken: String) : CacheData
    @JvmInline
    value class Public(override val url: String) : CacheData
}

expect class CacheSize(width: Int, height: Int)


@Composable
expect fun CacheImage(data: CacheData, modifier: Modifier = Modifier, size: CacheSize = CacheSize(-1, -1), loadingFallback: @Composable (() -> Unit)? = null)