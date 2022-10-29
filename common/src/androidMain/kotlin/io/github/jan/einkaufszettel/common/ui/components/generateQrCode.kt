package io.github.jan.einkaufszettel.common.ui.components

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.github.g0dkar.qrcode.QRCode

actual fun generateQrCode(text: String): ImageBitmap {
    return (QRCode(text)
        .render()
        .nativeImage() as Bitmap).asImageBitmap()
}