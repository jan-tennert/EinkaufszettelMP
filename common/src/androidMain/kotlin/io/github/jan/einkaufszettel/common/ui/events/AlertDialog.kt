package io.github.jan.einkaufszettel.common.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun AlertDialog(message: String, confirmButton: @Composable () -> Unit, close: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = close,
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                confirmButton()
            }
        },
        text = {
            Text(message)
        }
    )
}