package io.github.jan.einkaufszettel.common.ui.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

interface UIEvent {

    @Composable
    fun show(close: () -> Unit)

    class Alert(private val message: String) : UIEvent {

        @Composable
        override fun show(close: () -> Unit) {
            AlertDialog(message, { Button(close) { Text("Ok") } }, close)
        }

    }

    class Snackbar(private val message: String) : UIEvent {

        @Composable
        override fun show(close: () -> Unit) {
            LaunchedEffect(Unit) {
                delay(2000)
                close()
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Snackbar(modifier = Modifier.padding(10.dp)) {
                    Text(message)
                }
            }
        }

    }

}

@Composable
expect fun AlertDialog(message: String, confirmButton: @Composable () -> Unit, close: () -> Unit)