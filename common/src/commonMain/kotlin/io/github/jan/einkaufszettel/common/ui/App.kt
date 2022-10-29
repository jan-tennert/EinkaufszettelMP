package io.github.jan.einkaufszettel.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.screen.RootScreen
import io.github.jan.einkaufszettel.common.ui.theme.EinkaufszettelTheme
import io.github.jan.supabase.CurrentPlatformTarget
import io.github.jan.supabase.PlatformTarget

@Composable
fun App(viewModel: EinkaufszettelViewModel) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    LaunchedEffect(Unit) {
        if(CurrentPlatformTarget == PlatformTarget.DESKTOP) {
            viewModel.retrieveLatestVersion()
        }
    }
    EinkaufszettelTheme(darkMode) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            RootScreen(viewModel)
        }
    }
}
