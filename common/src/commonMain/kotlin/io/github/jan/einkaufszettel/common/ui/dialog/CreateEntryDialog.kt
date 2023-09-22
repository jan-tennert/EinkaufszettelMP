package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.LocalThemeSettings
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.handleEnter
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun CreateEntryDialog(shopId: Long, viewModel: EinkaufszettelViewModel, placeholder: String = "", close: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    Dialog(close, "Eintrag erstellen", darkMode) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(10)).padding(10.dp)
        ) {
            var content by remember { mutableStateOf(placeholder) }
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                singleLine = true,
                label = { Text("Eintrag") },
                keyboardActions = KeyboardActions(onDone = { viewModel.createEntry(shopId, content); close() }),
                modifier = Modifier.onPreviewKeyEvent {
                    it.handleEnter { viewModel.createEntry(shopId, content); close(); }
                }
            )
            Button(
                onClick = {
                    viewModel.createEntry(shopId, content)
                    close()
                },
                modifier = Modifier.padding(top = MaterialTheme.topPadding)
            ) {
                Text("Erstellen")
            }
        }
    }
}

@Composable
fun EditEntryDialog(oldContent: String, onEdit: (content: String) -> Unit, close: () -> Unit) {
    val darkMode = LocalThemeSettings.current.darkMode
    Dialog(close, "Eintrag bearbeiten", darkMode) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp)),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(20.dp)
            ) {
                var content by remember { mutableStateOf(oldContent) }
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    singleLine = true,
                    label = { Text("Eintrag") },
                    keyboardActions = KeyboardActions(onDone = { onEdit(content); close() }),
                    modifier = Modifier.onPreviewKeyEvent {
                        it.handleEnter { onEdit(content); close() }
                        false
                    }
                )
                Button(
                    onClick = {
                        onEdit(content)
                        close()
                    },
                    modifier = Modifier.padding(top = MaterialTheme.topPadding)
                ) {
                    Text("Speichern")
                }
            }
        }
    }
}