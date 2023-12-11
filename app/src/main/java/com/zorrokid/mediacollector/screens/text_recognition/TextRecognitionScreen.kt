package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import android.content.Context
import android.graphics.PointF
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.zorrokid.mediacollector.common.composable.PermissionDialog
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import com.zorrokid.mediacollector.common.util.adjustSize

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    TextRecognitionScreenContent(
        uiState = uiState,
        onStartTextRecognition = viewModel::startTextRecognition,
        onPointerOffsetChanged = viewModel::onPointerOffsetChanged,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreenContent(
    modifier: Modifier = Modifier,
    uiState: TextRecognitionUiState,
    onStartTextRecognition: (Context, LifecycleCameraController, LifecycleOwner, PreviewView) -> Unit,
    onPointerOffsetChanged: (Offset) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    if (cameraPermissionState.status.isGranted) {
        CameraPreview(modifier, onStartTextRecognition, uiState, onPointerOffsetChanged)
    }
    else {
        NoPermissionScreen(
            modifier = modifier,
            permissionStatus = cameraPermissionState.status,
            onPermissionRequested = {
                cameraPermissionState.launchPermissionRequest()
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoPermissionScreen(
    modifier: Modifier = Modifier,
    permissionStatus: PermissionStatus,
    onPermissionRequested: () -> Unit,
) {

    val openPermissionDialog = remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(text = "Camera permission is needed to scan text.")
        Button(onClick = {
            openPermissionDialog.value = true
        }) {
            Text("Permissions")
        }
    }

    when {
        openPermissionDialog.value -> {
            val message = if (permissionStatus.shouldShowRationale)
                "Text recognition feature requires camera permission."
            else
                "Text recognition feature requires camera permission. Please go to settings and enable camera permission."
            PermissionDialog(
                onLaunchPermissionRequest = {
                    openPermissionDialog.value = false
                    onPermissionRequested()
                },
                message = message,
                onDismiss = {
                    openPermissionDialog.value = false
                },
            )
        }
    }
}

@androidx.annotation.OptIn(TransformExperimental::class) @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onStartTextRecognition: (Context, LifecycleCameraController, LifecycleOwner, PreviewView) -> Unit,
    uiState: TextRecognitionUiState,
    onPointerOffsetChanged: (Offset) -> Unit = {},
){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
    val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }

    Scaffold(
        content = { padding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.BottomCenter
            ) {
                Column (
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .drawWithContent {
                            drawContent()

                            // TODO https://github.com/chouaibMo/MLKit-Jetpack-Compose/blob/master/app/src/main/java/com/chouaibmo/mlkitcompose/presentation/common/utils/PositionUtils.kt

                            uiState.recognizedText.textBlocks.forEach { textBlock ->
                                textBlock.lines.forEach { line ->
                                    line.elements.forEach { element ->
                                        val point = adjustPoint(
                                            MyPoint(
                                                element.boundingBox?.left?.toFloat() ?: 0f,
                                                element.boundingBox?.top?.toFloat() ?: 0f
                                            ),
                                            uiState.imageWidth,
                                            uiState.imageHeight,
                                            screenWidth.value,
                                            screenHeight.value
                                        )
                                        val size = adjustSize(
                                            Size(
                                                element.boundingBox?.width()?.toFloat() ?: 0f,
                                                element.boundingBox?.height()?.toFloat() ?: 0f
                                            ),
                                            uiState.imageWidth,
                                            uiState.imageHeight,
                                            screenWidth.value,
                                            screenHeight.value
                                        )

                                        drawRect(
                                            color = Color.Red,
                                            topLeft = Offset(
                                                x = point.x,
                                                y = point.y
                                            ),
                                            size = size,
                                        )
                                    }
                                }
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                onPointerOffsetChanged(offset)
                            }
                        }
                ){

                // Using AndroidView since Jetpack Compose not currently supporting CameraX
                    AndroidView(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(padding),
                        factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                            scaleType = PreviewView.ScaleType.FILL_START
                        }.also { previewView ->
                            onStartTextRecognition(
                                context,
                                cameraController,
                                lifecycleOwner,
                                previewView
                            )
                        }
                    })
                }
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = uiState.recognizedText.text
                )
            }
        }
    )
}

