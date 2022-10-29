package io.github.jan.einkaufszettel.common.ui

import androidx.compose.runtime.Composable

expect class URLService {

    fun openURL(url: String)

}

@Composable
expect fun rememberUrlService(): URLService