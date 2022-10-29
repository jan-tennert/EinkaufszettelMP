package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.Person
import io.github.jan.einkaufszettel.common.ui.theme.topPadding

@Composable
fun ProfileCreateScreen(viewModel: EinkaufszettelViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        var username by remember { mutableStateOf("") }
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Benutzername") },
            singleLine = true,
            leadingIcon = { Icon(LocalIcon.Person, "Name") },
        )
        Button(
            onClick = {
                viewModel.createProfile(username)
            },
            modifier = Modifier.padding(top = MaterialTheme.topPadding)
        ) {
            Text("Account erstellen")
        }
    }
}