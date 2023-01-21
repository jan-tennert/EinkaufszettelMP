package io.github.jan.einkaufszettel.common.ui.screen

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.NutritionCard
import io.github.jan.einkaufszettel.common.ui.dialog.CameraPreview
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun BarCodeScreen(viewModel: EinkaufszettelViewModel) {
    val nutritionData by viewModel.nutritionData.collectAsState()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    println(cameraPermissionState.status)
    when(cameraPermissionState.status) {
        is PermissionStatus.Denied -> {
            SideEffect {
                cameraPermissionState.launchPermissionRequest()
            }
        }
        PermissionStatus.Granted -> {
            if(nutritionData != null) {
                NutritionCard(nutritionData!!) { viewModel.nutritionData.value = null }
            } else {
                CameraPreview {
                    try {
                        viewModel.getNutritionFor(it)
                        true
                    } catch (e: IllegalArgumentException) {
                        viewModel.events.add(UIEvent.Alert("Kein gültiger QR Code"))
                        false
                    }
                }
            }
        }
    }
}