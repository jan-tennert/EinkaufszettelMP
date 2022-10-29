package io.github.jan.einkaufszettel.common.ui.components

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.github.jan.einkaufszettel.common.data.remote.FileInfo

@Composable
actual fun ImageChooser(show: Boolean, onSelect: (info: FileInfo) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri != null) {
            val extension = context.getExtensionFromUri(uri)
            val bytes = context.getBytesFromUri(uri)
            onSelect(FileInfo(extension, bytes!!))
        }
    }
    LaunchedEffect(show) {
        if(show) {
            launcher.launch("image/*")
        }
    }
}

fun Context.getExtensionFromUri(uri: Uri): String {
    var path = ""
    if (contentResolver != null) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            path = cursor.getString(idx)
            cursor.close()
        }
    }
    return path.split(".").last()
}

fun Context.getBytesFromUri(uri: Uri): ByteArray? {
    val inputStream = contentResolver.openInputStream(uri)
    return inputStream?.buffered()?.use { it.readBytes() }.also { inputStream?.close() }
}
