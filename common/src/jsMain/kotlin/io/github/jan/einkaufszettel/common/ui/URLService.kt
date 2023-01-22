package io.github.jan.einkaufszettel.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document

actual class URLService {
    actual fun openURL(url: String) {
        document.location?.href = url
    }

}

@Composable
actual fun rememberUrlService() = remember { URLService() }