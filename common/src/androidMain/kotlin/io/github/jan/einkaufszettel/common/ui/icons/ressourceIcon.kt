package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
actual fun resourceIcon(name: String): Painter {
    val context = LocalContext.current
    val nameWithoutExtension = name.substringBefore(".")
    val fontRes = context.resources.getIdentifier(nameWithoutExtension, "drawable", context.packageName)
    return painterResource(fontRes)
}