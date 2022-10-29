package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ProfileStatus
import io.github.jan.einkaufszettel.common.ui.dialog.ChangePasswordDialog
import io.github.jan.einkaufszettel.common.ui.dialog.ShareIdDialog
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.Person
import io.github.jan.einkaufszettel.common.ui.theme.topPadding
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.flow.map

@Composable
fun AccountScreen(viewModel: EinkaufszettelViewModel) {
    var shareId by remember { mutableStateOf(false) }
    var showPasswordChangeDialog by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        val currentUserName by viewModel.profileStatus.map { (it as ProfileStatus.Available).profile.username }.collectAsState("")
        var username by remember(currentUserName.isNotBlank()) { mutableStateOf(currentUserName) }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            leadingIcon = { Icon(LocalIcon.Person, "Name") },
            modifier = Modifier.focusRequester(focusRequester)
        )
        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.updateUsername(username)
            },
            modifier = Modifier.padding(top = MaterialTheme.topPadding)
        ) {
            Text("Speichern")
        }
        Button(
            onClick = {
                shareId = true
            },
            modifier = Modifier.padding(top = MaterialTheme.topPadding)
        ) {
            Text("Eigene Id teilen")
        }
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    showPasswordChangeDialog = true
                },
            ) {
                Text("Password ändern")
            }
            Button(
                onClick = {
                    viewModel.logout()
                },
                modifier = Modifier.padding(10.dp),
            ) {
                Text("Ausloggen")
            }
        }
    }

    if(shareId) {
        ShareIdDialog(close = { shareId = false }, id = viewModel.supabaseClient.gotrue.currentSessionOrNull()!!.user!!.id, viewModel)
    }

    if(showPasswordChangeDialog) {
        ChangePasswordDialog(viewModel, close = { showPasswordChangeDialog = false })
    }
}