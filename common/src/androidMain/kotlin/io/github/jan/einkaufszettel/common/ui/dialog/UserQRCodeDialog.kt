package io.github.jan.einkaufszettel.common.ui.dialog

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import io.github.jan.einkaufszettel.common.BarCodeAnalyser
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun UserQRCodeDialog(
    viewModel: EinkaufszettelViewModel,
    close: () -> Unit
) {
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    when(cameraPermissionState.status) {
        is PermissionStatus.Denied -> {
            SideEffect {
                cameraPermissionState.launchPermissionRequest()
            }
        }
        PermissionStatus.Granted -> {
            Dialog(close, "", darkMode = darkMode) {
                Box(modifier = Modifier.size(200.dp)) {
                    CameraPreview {
                        try {
                            UUID.fromString(it)
                            viewModel.retrieveSingleProfile(it)
                            close()
                            true
                        } catch(e: IllegalArgumentException) {
                            viewModel.events.add(UIEvent.Alert("Kein gültiger QR Code"))
                            false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(onBarCode: (String) -> Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var enableTorch by remember { mutableStateOf(false) }
    val cameraSelector: CameraSelector = remember { CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build() }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraProvider: ProcessCameraProvider = remember { cameraProviderFuture.get() }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider.unbindAll()
        }
    }

    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        update = { previewView ->
            cameraProviderFuture.addListener({
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                    barcodes.firstOrNull()?.rawValue?.let {
                        if (onBarCode(it)) {
                            cameraProvider.unbindAll()
                        }
                    }
                }
                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    ).also {
                        if(it.cameraInfo.hasFlashUnit()) {
                            it.cameraControl.enableTorch(enableTorch)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )

    /*Box(modifier = Modifier
        .fillMaxSize()
        .padding(end = 20.dp, bottom = 20.dp), contentAlignment = Alignment.BottomEnd) {
        Icon(if(!enableTorch) MIcon.FlashOff else MIcon.FlashOn, "", modifier = Modifier.clickable {
            enableTorch = !enableTorch
        })
    }*/
}
