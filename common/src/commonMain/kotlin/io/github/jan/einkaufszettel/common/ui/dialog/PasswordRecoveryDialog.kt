package io.github.jan.einkaufszettel.common.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.Mail
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun PasswordRecoveryDialog(viewModel: EinkaufszettelViewModel, email: String, close: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    Dialog(close, "Passwort vergessen", darkMode) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(MaterialTheme.topPadding)
            ) {
                var recoveryEmail by remember { mutableStateOf(email) }
                OutlinedTextField(
                    value = recoveryEmail,
                    onValueChange = { recoveryEmail = it },
                    singleLine = true,
                    label = { Text("E-Mail") },
                    leadingIcon = { Icon(LocalIcon.Mail, "Email") }
                )

                Button(
                    onClick = {
                        viewModel.sendPasswordRecovery(recoveryEmail)
                        close()
                    },
                    modifier = Modifier.padding(top = MaterialTheme.topPadding)
                ) {
                    Text("Passwort zurücksetzen")
                }
            }
        }
    }
}