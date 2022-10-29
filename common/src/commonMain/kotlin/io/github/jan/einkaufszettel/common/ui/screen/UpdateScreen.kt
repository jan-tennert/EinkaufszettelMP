package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel

@Composable
fun UpdateScreen(viewModel: EinkaufszettelViewModel, ignore: () -> Unit) {
    val progress by viewModel.downloadProgress.collectAsState()
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ElevatedCard {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                if(progress == 0f) {
                    Text("Neue Version verfügbar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = { viewModel.downloadLatestVersion() },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text("Aktualisieren")
                        }
                        Button(
                            onClick = ignore,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text("Abbrechen")
                        }
                    }
                } else {
                    Text("Neue Version wird heruntergeladen", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterHorizontally))
                    LinearProgressIndicator(progress = progress, modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}