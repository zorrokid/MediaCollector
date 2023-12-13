package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
import com.zorrokid.mediacollector.screens.add_item.AddItemViewModel
import java.util.concurrent.Executor

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    sharedViewModel: AddItemViewModel,
    popUp: () -> Unit,
) {
    val uiState by viewModel.uiState

    TextRecognitionScreenContent(
        uiState = uiState,
        onStartTextRecognition = viewModel::startTextRecognition,
        onPointerOffsetChanged = viewModel::onPointerOffsetChanged,
        onTouchEvent = viewModel::onTouchEvent,
        onStopTextRecognition = viewModel::onStopTextRecognition,
        onTextRecognitionResultReady = sharedViewModel::onTextRecognitionResultReady,
        popUp = popUp,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreenContent(
    modifier: Modifier = Modifier,
    uiState: TextRecognitionUiState,
    onStartTextRecognition: (Context, LifecycleCameraController, LifecycleOwner, PreviewView) -> Unit,
    onPointerOffsetChanged: (Offset) -> Unit,
    onTouchEvent: (Offset, LifecycleCameraController) -> Unit,
    onStopTextRecognition: (LifecycleCameraController, (String) -> Unit) -> Unit,
    onTextRecognitionResultReady: (String) -> Unit,
    popUp: () -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    if (cameraPermissionState.status.isGranted) {
        CameraPreview(
            modifier,
            onStartTextRecognition,
            uiState,
            onPointerOffsetChanged,
            onTouchEvent,
            onStopTextRecognition,
            onTextRecognitionResultReady,
            popUp,
        )
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
    onTouchEvent: (Offset, LifecycleCameraController) -> Unit,
    onStopTextRecognition: (LifecycleCameraController, (String) -> Unit) -> Unit,
    onTextRecognitionResultReady: (String) -> Unit,
    popUp: () -> Unit,
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
        floatingActionButton = {
                               FloatingActionButton(
                                   onClick = {
                                       onStopTextRecognition(cameraController, onTextRecognitionResultReady)
                                       popUp()
                                 },
                              ){
                                   Icon(Icons.Filled.Done, "Add")
                               }
        },
        content = { padding ->
            /*Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.BottomCenter
            ) {*/
                Column (
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(padding)
                       .drawWithContent {
                            drawContent()

                            /*drawRect(
                               color = Color.Green,
                               topLeft = Offset(
                                   x = 0.0f,
                                   y = 0.0f
                               ),
                               size = Size(screenWidth.value.toFloat(), screenHeight.value.toFloat())
                           )*/

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
                                            screenHeight.value,
                                            screenWidth.value,
                                        )
                                        val size = adjustSize(
                                            Size(
                                                element.boundingBox
                                                    ?.width()
                                                    ?.toFloat() ?: 0f,
                                                element.boundingBox
                                                    ?.height()
                                                    ?.toFloat() ?: 0f
                                            ),
                                            uiState.imageWidth,
                                            uiState.imageHeight,
                                            screenHeight.value,
                                            screenWidth.value,
                                        )

                                        Log.d(
                                            "TextRecognitionScreen",
                                            "Picture size: ${uiState.imageWidth}x${uiState.imageHeight} " +
                                                    "screen size: ${screenWidth.value}x${screenHeight.value} " +
                                                    "original point: (${element.boundingBox?.left},${element.boundingBox?.top})" +
                                                    "scaled point: (${point.x},${point.y}) " +
                                                    "original size: ${element.boundingBox?.width()}x${element.boundingBox?.height()} " +
                                                    "scaled size: ${size.width}x${size.height}"
                                        )

// Picture size: 640x480
// screen size: 1080x2106 => scale factor for width: 1.6875, scale factor for height: 4.3875
// original point: (153,304)
// scaled point: (258.1875,1333.7999)
// original size: 96x33
// scaled size: 162.0x144.78749

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
                    /*
tap to focus example:
https://proandroiddev.com/android-camerax-tap-to-focus-pinch-to-zoom-zoom-slider-eb88f3aa6fc6

previewView.setOnTouchListener(OnTouchListener { view: View, motionEvent: MotionEvent ->
    when (motionEvent.action) {
        MotionEvent.ACTION_DOWN -> return@setOnTouchListener true
        MotionEvent.ACTION_UP -> {
            // Get the MeteringPointFactory from PreviewView
            val factory = mPreviewView.getMeteringPointFactory()

            // Create a MeteringPoint from the tap coordinates
            val point = factory.createPoint(motionEvent.x, motionEvent.y)

            // Create a MeteringAction from the MeteringPoint, you can configure it to specify the metering mode
            val action = FocusMeteringAction.Builder(point).build()

            // Trigger the focus and metering. The method returns a ListenableFuture since the operation
            // is asynchronous. You can use it get notified when the focus is successful or if it fails.
            cameraControl.startFocusAndMetering(action)

            return@setOnTouchListener true
        }
        else -> return@setOnTouchListener false
    }
})
                             */
                        }
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = uiState.recognizedText.text
                )
            //}

            //}
        }
    )
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                //.rotateBitmap(image.imageInfo.rotationDegrees)

            onPhotoCaptured(correctedBitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}
