package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import android.content.Context
import android.widget.LinearLayout
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.zorrokid.mediacollector.common.composable.PermissionDialog
import com.zorrokid.mediacollector.common.text_recognition.TextRecognitionAnalyzer

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
) {
    TextRecognitionScreenContent()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreenContent(
    modifier: Modifier = Modifier,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    val openPermissionDialog = remember { mutableStateOf(false) }

    if (cameraPermissionState.status.isGranted) {
        CameraPreview()
    }
    else {
        Column {
            Text(text = "Camera permission is needed to scan text.")
            Button(onClick = {
                openPermissionDialog.value = true
            }) {
                Text("Permissions")
            }
        }

    }

    when {
        openPermissionDialog.value -> {
            val message = if (cameraPermissionState.status.shouldShowRationale)
                "Text recognition feature requires camera permission."
            else
                "Text recognition feature requires camera permission. Please go to settings and enable camera permission."
            PermissionDialog(
                onLaunchPermissionRequest = {
                    openPermissionDialog.value = false
                    cameraPermissionState.launchPermissionRequest()
                },
                message = message,
                onDismiss = {
                    openPermissionDialog.value = false
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreview(modifier: Modifier = Modifier){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    var detectedText: String by remember { mutableStateOf("No text detected yet..") }
    fun onTextUpdated(text: String) {
        println("Detected text: $text")
    }


    Scaffold(
        content = { padding ->
            Box(
                modifier = modifier
                .fillMaxSize()
                .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.BottomCenter
            ) {
                // Using AndroidView since Jetpack Compose not currently supporting CameraX
                AndroidView(
                    modifier = modifier.fillMaxSize().padding(padding),
                    factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        startTextRecognition(
                            context = context,
                            cameraController = cameraController,
                            lifecycleOwner = lifecycleOwner,
                            previewView = previewView,
                            onDetectedText = { text ->
                                detectedText = text
                            }
                        )
                    }
                })
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = detectedText
                )
            }
        }
    )
}

fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedText: (String) -> Unit,
) {
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(
            onDetectedTextUpdated = onDetectedText
        )
    )
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}