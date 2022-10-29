package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.github.g0dkar.qrcode.QRCode

actual fun generateQrCode(text: String): ImageBitmap {
    return org.jetbrains.skia.Image.makeFromEncoded(
        QRCode(text)
        .render()
        .getBytes()).toComposeImageBitmap()
}