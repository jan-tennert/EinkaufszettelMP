package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.components.PasswordField
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun ChangePasswordDialog(viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    Dialog(close, "Passwort ändern", darkMode) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(MaterialTheme.topPadding)
            ) {
                var newPassword by remember { mutableStateOf("") }
                PasswordField(
                    password = newPassword,
                    onPasswordChanged = { newPassword = it },
                )

                Button(
                    onClick = {
                        viewModel.changePasswordTo(newPassword)
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