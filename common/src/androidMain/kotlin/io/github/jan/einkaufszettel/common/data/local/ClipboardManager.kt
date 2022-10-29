package io.github.jan.einkaufszettel.common.data.local

import android.content.ClipData
import android.content.Context

actual class ClipboardManager(private val context: Context) {

    actual fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("User Id", text)
        clipboard.setPrimaryClip(clip)
    }

}