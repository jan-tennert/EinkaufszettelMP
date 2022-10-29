package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.ui.events.AlertDialog

@Composable
fun DeleteDialog(onDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        message = "Möchtest du den Eintrag wirklich löschen?",
        confirmButton = {
            Button(onClick = {
                onDelete()
                onDismiss()
            }) {
                Text("Löschen")
            }
            Button(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Abbrechen")
            }
        },
        close = onDismiss
    )
}