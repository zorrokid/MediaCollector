package com.zorrokid.mediacollector.common.text_recognition.composable

import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zorrokid.mediacollector.common.text_recognition.controller.TextRecognitionController
import com.zorrokid.mediacollector.common.text_recognition.model.TextRecognitionStatus
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import com.zorrokid.mediacollector.model.TextBlock
import com.zorrokid.mediacollector.model.TextRecognitionInfo

@androidx.annotation.OptIn(TransformExperimental::class) @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    uiState: TextRecognitionStatus,
    onDetectedTextUpdated: (TextRecognitionInfo) -> Unit,
    onTextRegognitinoFinished: () -> Unit,
){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    fun drawRectangles(
        drawScope: ContentDrawScope,
        screenSize: Size,
        imageSize: Size,
        recognizedText: List<TextBlock>,
    ) {
        recognizedText.forEach { textBlock ->

            val points = textBlock.points.map {
                adjustPoint(MyPoint(it.x, it.y), imageSize, screenSize, uiState.rotation)
            }

            points.forEachIndexed { index, point ->
                val end = if (index == points.size - 1) {
                    points[0]
                } else {
                    points[index + 1]
                }
                drawScope.drawLine(
                    color = Color.Red,
                    Offset(point.x, point.y),
                    Offset(end.x, end.y)
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    TextRecognitionController.onStopTextRecognition(
                        cameraController,
                        onTextRegognitinoFinished,
                    )
                  },
                ){
                Icon(Icons.Filled.Done, "Add")
            }
        },
        content = { padding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column (
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .drawWithContent {
                            // Modifier.drawWithContent lets you execute DrawScope operations before or after the content of the composable.
                            // Call drawContent to render the actual content of the composable.
                            drawContent()
                            val displayWidth = with(density) {
                                configuration.screenWidthDp.dp.roundToPx()
                            }
                            val displayHeight = with(density) {
                                configuration.screenHeightDp.dp.roundToPx()
                            }
                            drawRectangles(
                                this,
                                Size(
                                    displayWidth.toFloat(),
                                    displayHeight.toFloat()
                                ),
                                uiState.imageSize,
                                uiState.recognizedText,
                            )
                            val imageWidth = uiState.imageSize.width
                            val imageHeight = uiState.imageSize.height
                            this.drawRect(
                                color = Color.Red,
                                topLeft = Offset(0.0f, 0.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Green,
                                topLeft = Offset(0.0f, imageHeight - 100.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Blue,
                                topLeft = Offset(imageWidth - 100.0f, 0.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Yellow,
                                topLeft = Offset(imageWidth - 100.0f, imageHeight - 100.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Red,
                                topLeft = Offset(0.0f, 0.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Green,
                                topLeft = Offset(0.0f, displayHeight - 100.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Blue,
                                topLeft = Offset(displayWidth - 100.0f, 0.0f),
                                size = Size(100.0f, 100.0f)
                            )
                            this.drawRect(
                                color = Color.Yellow,
                                topLeft = Offset(displayWidth - 100.0f, displayHeight - 100.0f),
                                size = Size(100.0f, 100.0f)
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
                                TextRecognitionController.startTextRecognition(
                                    context,
                                    cameraController,
                                    lifecycleOwner,
                                    previewView,
                                    onDetectedTextUpdated,
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}