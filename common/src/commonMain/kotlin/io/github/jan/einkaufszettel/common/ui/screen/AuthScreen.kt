package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.handleEnter
import io.github.jan.einkaufszettel.common.ui.components.GoogleButton
import io.github.jan.einkaufszettel.common.ui.components.PasswordField
import io.github.jan.einkaufszettel.common.ui.dialog.PasswordRecoveryDialog
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.Mail
import io.github.jan.einkaufszettel.common.ui.theme.topPadding
import io.github.jan.supabase.gotrue.providers.builtin.Email.Config

@Composable
fun AuthScreen(viewModel: EinkaufszettelViewModel) {
    var signUp by remember { mutableStateOf(false) }
    var showPasswordRecoveryDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var password by remember { mutableStateOf("") }
        val passwordFocus = remember { FocusRequester() }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            label = { Text("E-Mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
            leadingIcon = { Icon(LocalIcon.Mail, "Mail") },
        )
        PasswordField(
            password = password,
            onPasswordChanged = { password = it },
            modifier = Modifier.focusRequester(passwordFocus)
                .padding(top = MaterialTheme.topPadding)
                .onPreviewKeyEvent {
                    it.handleEnter {
                        authenticate(
                            signUp,
                            viewModel,
                            email,
                            password
                        )
                    }
                    false
                },
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = {
                authenticate(
                    signUp,
                    viewModel,
                    email,
                    password
                )
            }),
        )
        Button(
            onClick = { authenticate(signUp, viewModel, email, password) },
            modifier = Modifier.padding(top = MaterialTheme.topPadding)
        ) {
            Text(if (signUp) "Registrieren" else "Anmelden")
        }
        GoogleButton(
            text = if (signUp) "Mit Google registrieren" else "Mit Google anmelden"
        ) { viewModel.loginWithGoogle() }

        TextButton(onClick = { showPasswordRecoveryDialog = true }, modifier = Modifier.padding(top = MaterialTheme.topPadding)) {
            Text("Password vergessen?")
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        TextButton(onClick = { signUp = !signUp }) {
            Text(if (signUp) "Hast du bereits einen Account? Anmelden" else "Noch keinen Account? Registrieren")
        }
    }

    if(showPasswordRecoveryDialog) {
        PasswordRecoveryDialog(viewModel, close = { showPasswordRecoveryDialog = false }, email = email)
    }
}

private fun authenticate(
    signUp: Boolean,
    viewModel: EinkaufszettelViewModel,
    email: String,
    password: String
) {
    val config: Config.() -> Unit = {
        this.email = email
        this.password = password
    }
    if (signUp) {
        viewModel.signUpWithEmail(config)
    } else {
        viewModel.loginWithEmail(config)
    }
}