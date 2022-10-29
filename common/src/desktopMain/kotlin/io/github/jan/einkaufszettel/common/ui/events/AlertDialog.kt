package io.github.jan.einkaufszettel.common.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState

@Composable
actual fun AlertDialog(message: String, confirmButton: @Composable () -> Unit, close: () -> Unit) {
    Dialog(close, state = rememberDialogState(height = 150.dp), title = "Information", resizable = false) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(message, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                confirmButton()
            }
        }
    }
}