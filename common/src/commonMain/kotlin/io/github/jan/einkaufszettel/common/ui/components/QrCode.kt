package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
fun QrCode(text: String, modifier: Modifier = Modifier) {
    val qrCode by produceState<ImageBitmap?>(null) {
        value = generateQrCode(text)
    }
    qrCode?.let {
        Image(bitmap = it, contentDescription = "QR Code", modifier = modifier)
    }
}

expect fun generateQrCode(text: String): ImageBitmap