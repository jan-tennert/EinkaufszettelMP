package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.handleEnter
import io.github.jan.einkaufszettel.common.ui.dialog.Dialog
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun CreateEntryDialog(shopId: Long, viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    Dialog(close, "Eintrag erstellen", darkMode) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var content by remember { mutableStateOf("") }
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                singleLine = true,
                label = { Text("Eintrag") },
                keyboardActions = KeyboardActions(onDone = { viewModel.createProduct(shopId, content); close() }),
                modifier = Modifier.onPreviewKeyEvent {
                    it.handleEnter { viewModel.createProduct(shopId, content); close() }
                    false
                }
            )
            Button(
                onClick = {
                    viewModel.createProduct(shopId, content)
                    close()
                },
                modifier = Modifier.padding(top = MaterialTheme.topPadding)
            ) {
                Text("Erstellen")
            }
        }
    }
}