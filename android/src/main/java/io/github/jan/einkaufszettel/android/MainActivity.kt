package io.github.jan.einkaufszettel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.App
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.handleDeeplinks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val viewModel: EinkaufszettelViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.supabaseClient.handleDeeplinks(intent)
        addLifecycleCallback()
        setContent {
            MaterialTheme {
                App(viewModel)
            }
        }
    }

    private fun addLifecycleCallback() {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(object : DefaultLifecycleObserver {

            private var job: Job? = null

            override fun onStart(owner: LifecycleOwner) {
                job = lifecycle.coroutineScope.launch {
                    while(isActive && viewModel.supabaseClient.gotrue.currentSessionOrNull() == null) {
                        delay(500)
                    }
                    viewModel.retrieveAll()
                    viewModel.retrieveProfile(false)
                    viewModel.connectToRealtime()
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                job?.cancel()
                viewModel.disconnectFromRealtime()
            }

        })
    }

}