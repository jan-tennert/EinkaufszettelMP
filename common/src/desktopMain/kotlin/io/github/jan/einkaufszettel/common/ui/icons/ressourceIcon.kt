package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
actual fun resourceIcon(name: String): Painter = painterResource("drawable/$name")