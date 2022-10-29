package io.github.jan.einkaufszettel.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Desktop
import java.net.URI

actual class URLService {

    actual fun openURL(url: String) {
        Desktop.getDesktop().browse(URI(url))
    }

}

@Composable
actual fun rememberUrlService() = remember { URLService() }