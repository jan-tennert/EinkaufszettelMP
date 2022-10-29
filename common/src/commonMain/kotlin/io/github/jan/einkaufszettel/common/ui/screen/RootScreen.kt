package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ProfileStatus
import io.github.jan.supabase.CurrentPlatformTarget
import io.github.jan.supabase.PlatformTarget
import io.github.jan.supabase.gotrue.SessionStatus

@Composable
fun RootScreen(viewModel: EinkaufszettelViewModel) {
    val sessionStatus by viewModel.sessionStatus.collectAsState()
    when(sessionStatus) {
        SessionStatus.LoadingFromStorage -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        SessionStatus.NotAuthenticated -> {
            AuthScreen(viewModel)
        }
        SessionStatus.NetworkError -> MainScreen(viewModel)
        is SessionStatus.Authenticated -> {
            val profileStatus by viewModel.profileStatus.collectAsState()
            LaunchedEffect(Unit) {
                if(profileStatus !is ProfileStatus.Available) viewModel.retrieveProfile()
                if(CurrentPlatformTarget == PlatformTarget.DESKTOP) {
                    viewModel.retrieveProducts()
                    viewModel.retrieveShops()
                    viewModel.retrieveProfile(false)
                    viewModel.connectToRealtime()
                }
            }
            when(profileStatus) {
                is ProfileStatus.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProfileStatus.Available -> {
                    MainScreen(viewModel)
                }
                is ProfileStatus.NotExisting -> {
                   ProfileCreateScreen(viewModel)
                }
                else -> {
                    println("not tried")
                }
            }
        }
    }
    val events = viewModel.events
    events.forEachIndexed { index, uiEvent ->
        uiEvent.show { events.removeAt(index) }
    }
}