package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.theme.EinkaufszettelTheme

@Composable
actual fun Dialog(
    close: () -> Unit,
    title: String,
    darkMode: EinkaufszettelSettings.DarkMode,
    width: Int,
    height: Int,
    content: @Composable () -> Unit
) {
    Box(Modifier.size(DpSize(width = width.dp, height = height.dp))) {
        EinkaufszettelTheme(darkMode) {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}