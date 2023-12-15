package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.zorrokid.mediacollector.common.composable.PermissionDialog
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import com.zorrokid.mediacollector.common.util.adjustSize
import com.zorrokid.mediacollector.screens.add_or_edit_item.AddOrEditItemViewModel

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    sharedViewModel: AddOrEditItemViewModel,
    popUp: () -> Unit,
) {
    val uiState by viewModel.uiState

    TextRecognitionScreenContent(
        uiState = uiState,
        onStartTextRecognition = viewModel::startTextRecognition,
        onStopTextRecognition = viewModel::onStopTextRecognition,
        onTextSelected = sharedViewModel::onTextSelected,
        popUp = popUp,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreenContent(
    modifier: Modifier = Modifier,
    uiState: TextRecognitionUiState,
    onStartTextRecognition: (Context, LifecycleCameraController, LifecycleOwner, PreviewView) -> Unit,
    onStopTextRecognition: (LifecycleCameraController) -> Unit,
    onTextSelected: (List<String>, () -> Unit) -> Unit,
    popUp: () -> Unit,
) {

    if (uiState.recognitionDone) {
        TextScanResultSelector(
            modifier = modifier,
            recognizedText = uiState.recognizedText,
            onTextSelected = onTextSelected,
            popUp = popUp,
        )
    }
    else {
        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                modifier,
                onStartTextRecognition,
                uiState,
                onStopTextRecognition,
            )
        } else {
            NoPermissionScreen(
                modifier = modifier,
                permissionStatus = cameraPermissionState.status,
                onPermissionRequested = {
                    cameraPermissionState.launchPermissionRequest()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScanResultSelector(
    modifier: Modifier = Modifier,
    recognizedText: Text,
    onTextSelected: (List<String>, () -> Unit) -> Unit,
    popUp: () -> Unit,
) {
    val selectedTexts = remember { mutableStateOf(emptyList<String>()) }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onTextSelected(selectedTexts.value, popUp) },
            ) {
                Icon(Icons.Filled.Done, "Add")
            }
        },
        content = { padding ->
            LazyColumn (modifier = modifier.padding(padding)) {
                itemsIndexed(recognizedText.textBlocks) { index, textBlock ->
                    TextScanResultCard(modifier, textBlock, onTextSelected =  {
                        selectedTexts.value = selectedTexts.value + it
                    }, index)
                }
            }
        },
    )
}

@Composable
fun TextScanResultCard(
    modifier: Modifier = Modifier,
    textBlock: TextBlock,
    onTextSelected: (String) -> Unit,
    index: Int,
) {
    val isSelected = remember { mutableStateOf(false) }
    Card(modifier = modifier) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(checked = isSelected.value, onCheckedChange = {
                    isSelected.value = it
                    if (it) {
                        onTextSelected(textBlock.text)
                    }
                })
                textBlock.lines.forEach { line ->
                    line.elements.forEach { element ->
                        Text(text = element.text)
                    }
                }
            }
        }
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
    onStopTextRecognition: (LifecycleCameraController) -> Unit,
){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    val screenWidth = remember { mutableIntStateOf(context.resources.displayMetrics.widthPixels) }
    val screenHeight = remember { mutableIntStateOf(context.resources.displayMetrics.heightPixels) }

    fun drawRectangles(
        drawScope: ContentDrawScope,
        screenWidth: Int,
        screenHeight: Int,
        imageWidth: Int,
        imageHeight: Int,
        recognizedText: Text,
    ) {
        recognizedText.textBlocks.forEach { textBlock ->
            textBlock.lines.forEach { line ->
                line.elements.forEach { element ->
                    val point = adjustPoint(
                        MyPoint(
                            element.boundingBox?.left?.toFloat() ?: 0f,
                            element.boundingBox?.top?.toFloat() ?: 0f
                        ),
                        imageWidth,
                        imageHeight,
                        screenHeight,
                        screenWidth,
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
                        imageWidth,
                        imageHeight,
                        screenHeight,
                        screenWidth,
                    )

                    Log.d(
                        "TextRecognitionScreen",
                        "Picture size: ${uiState.imageWidth}x${uiState.imageHeight} " +
                                "screen size: ${screenWidth}x${screenHeight} " +
                                "original point: (${element.boundingBox?.left},${element.boundingBox?.top})" +
                                "scaled point: (${point.x},${point.y}) " +
                                "original size: ${element.boundingBox?.width()}x${element.boundingBox?.height()} " +
                                "scaled size: ${size.width}x${size.height}"
                    )

                    drawScope.drawRect(
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

    Scaffold(
        floatingActionButton = {
           FloatingActionButton(
               onClick = { onStopTextRecognition(cameraController) },
          ){
               Icon(Icons.Filled.Done, "Add")
           }
        },
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
                            // Modifier.drawWithContent lets you execute DrawScope operations before or after the content of the composable.
                            // Call drawContent to render the actual content of the composable.
                            drawContent()
                            drawRectangles(
                                this,
                                screenWidth.value,
                                screenHeight.value,
                                uiState.imageWidth,
                                uiState.imageHeight,
                                uiState.recognizedText,
                            )
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