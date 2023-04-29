package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.Einkaufszettel.VERSION
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.rememberUrlService

@Composable
fun SettingsScreen(viewModel: EinkaufszettelViewModel) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    val urlService = rememberUrlService()
    var showAccountSettings by remember { mutableStateOf(false) }
    if(showAccountSettings) {
        AccountScreen(viewModel) {
            showAccountSettings = false
        }
    } else {
        Column(modifier = Modifier.padding(7.dp)) {
            Text("Theme", fontWeight = FontWeight.Bold)
            ThemeButton(darkMode, EinkaufszettelSettings.DarkMode.NOT_SET) {
                viewModel.setDarkMode(EinkaufszettelSettings.DarkMode.NOT_SET)
            }
            ThemeButton(darkMode, EinkaufszettelSettings.DarkMode.ON) {
                viewModel.setDarkMode(EinkaufszettelSettings.DarkMode.ON)
            }
            ThemeButton(darkMode, EinkaufszettelSettings.DarkMode.OFF) {
                viewModel.setDarkMode(EinkaufszettelSettings.DarkMode.OFF)
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button({
                    showAccountSettings = true
                }) {
                    Text("Account")
                }
                Button({
                    urlService.openURL("https://github.com/jan-tennert/EinkaufszettelMP/releases/tag/v$VERSION")
                }) {
                    Text("Neu in der Version $VERSION")
                }
            }
        }
    }
}

@Composable
private fun ThemeButton(currentDarkMode: EinkaufszettelSettings.DarkMode, setting: EinkaufszettelSettings.DarkMode, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentDarkMode == setting,
            onClick = {
                if(currentDarkMode != setting) {
                    onClick()
                }
            }
        )
        Text(when(setting) {
            EinkaufszettelSettings.DarkMode.NOT_SET -> "System"
            EinkaufszettelSettings.DarkMode.ON -> "Dunkel"
            EinkaufszettelSettings.DarkMode.OFF -> "Hell"
        })
    }
}