package io.github.jan.einkaufszettel.common.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity

actual class URLService(private val androidContext: Context) {

    actual fun openURL(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(androidContext, browserIntent, null)
    }

}

@Composable
actual fun rememberUrlService(): URLService {
    val context = LocalContext.current
    return remember {
        URLService(context)
    }
}