package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.components.QrCode
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun ShareIdDialog(close: () -> Unit, id: String, viewModel: EinkaufszettelViewModel) {
    var showQrCode by remember { mutableStateOf(false) }
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    if(!showQrCode) {
        Dialog(close, "Id teilen", darkMode) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.clipboardManager.copyToClipboard(id)
                        close()
                        viewModel.events.add(UIEvent.Snackbar("Id wurde in die Zwischenablage kopiert"))
                    }
                ) {
                    Text("Id kopieren")
                }
                Button(
                    onClick = { showQrCode = true },
                    modifier = Modifier.padding(MaterialTheme.topPadding)
                ) {
                    Text("Id als QR-Code anzeige")
                }
            }
        }
    } else {
        Dialog(close, width = 400, height = 400, darkMode = darkMode, title = "Id als QR-Code") {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(400.dp, 400.dp)) {
                QrCode(id, modifier = Modifier.clickable { showQrCode = false; close() })
            }
        }
    }
}