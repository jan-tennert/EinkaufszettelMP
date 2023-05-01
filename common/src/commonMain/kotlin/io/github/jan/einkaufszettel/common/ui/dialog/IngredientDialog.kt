package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings

@Composable
fun IngredientDialog(placeholder: String? = null, onAdd: (String) -> Unit, close: () -> Unit) {
    Dialog(close, title = "Zutaten", darkMode = EinkaufszettelSettings.DarkMode.ON) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp).background(MaterialTheme.colorScheme.background)
        ) {
            var name by remember { mutableStateOf(placeholder ?: "") }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Zutat") }
            )
            Button(onClick = {
                onAdd(name)
                close()
            }) {
                Text("Hinzufügen")
            }
        }
    }
}