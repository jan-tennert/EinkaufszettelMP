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
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.handleEnter
import io.github.jan.einkaufszettel.common.ui.theme.topPadding
import io.github.jan.supabase.CurrentPlatformTarget
import io.github.jan.supabase.PlatformTarget

sealed interface UserAddDialogState {
    object None : UserAddDialogState
    object WithId: UserAddDialogState
    object WithQRCode: UserAddDialogState
}

@Composable
fun UserAddDialog(viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    var state by remember { mutableStateOf<UserAddDialogState>(UserAddDialogState.None) }
    when(state) {
        UserAddDialogState.WithId -> {
            Dialog(close, "Benutzer hinzufügen", darkMode) {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(MaterialTheme.topPadding)
                    ) {
                        var id by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = id,
                            onValueChange = { id = it },
                            singleLine = true,
                            label = { Text("Benutzer-ID") },
                            keyboardActions = KeyboardActions(onDone = { viewModel.retrieveSingleProfile(id); close() }),
                            modifier = Modifier.onPreviewKeyEvent {
                                it.handleEnter { viewModel.retrieveSingleProfile(id); close() }
                                false
                            }
                        )
                        Button(
                            onClick = {
                                viewModel.retrieveSingleProfile(id)
                                close()
                            },
                            modifier = Modifier.padding(top = MaterialTheme.topPadding)
                        ) {
                            Text("Hinzufügen")
                        }
                    }
                }
            }
        }
        UserAddDialogState.WithQRCode -> {
            UserQRCodeDialog(viewModel, close)
        }
        UserAddDialogState.None -> {
            Dialog(close, "Benutzer hinzufügen", darkMode) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            state = UserAddDialogState.WithId
                        }
                    ) {
                        Text("User Id eingeben")
                    }
                    if(CurrentPlatformTarget != PlatformTarget.JVM) {
                        Button(
                            onClick = {
                                state = UserAddDialogState.WithQRCode
                            },
                            modifier = Modifier.padding(top = MaterialTheme.topPadding)
                        ) {
                            Text("QR Code scannen")
                        }
                    }
                }
            }
        }
    }
}