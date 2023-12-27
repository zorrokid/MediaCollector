package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import android.content.Context
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.composable.BasicTopAppBar
import com.zorrokid.mediacollector.common.composable.PermissionDialog
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import com.zorrokid.mediacollector.model.TextBlock
import com.zorrokid.mediacollector.model.TextLine
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
    recognizedText: List<TextBlock>,
    onTextSelected: (List<String>, () -> Unit) -> Unit,
    popUp: () -> Unit,
) {
    val selectedTexts = remember { mutableStateOf(emptyList<String>()) }

    var showSingleWordSelection = remember {
        mutableStateOf(false)
    }

    Scaffold (
        topBar = {
           BasicTopAppBar(titleResourceId = R.string.text_recognition, popUp = popUp)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onTextSelected(selectedTexts.value, popUp) },
            ) {
                Icon(Icons.Filled.Done, "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp)
            ) {
                SwitchWithLabel(
                    label = "Show single word selection",
                    checked = showSingleWordSelection.value,
                    onCheckedChange = {
                        showSingleWordSelection.value = it
                    },
                    modifier = modifier
                )
                LazyColumn (
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(recognizedText) { index, textBlock ->
                        TextScanResultCard(
                            modifier = modifier,
                            textBlock,
                            onTextSelected =  {
                                selectedTexts.value = selectedTexts.value + it
                            },
                            index,
                            showSingleWordSelection.value,
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun SwitchWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label,
            modifier = modifier.padding(horizontal = 8.dp))
    }
}

@Preview
@Composable
fun SwitchWithLabelPreview() {
    SwitchWithLabel(
        label = "Example label",
        checked = false,
        onCheckedChange = {},
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextScanResultCard(
    modifier: Modifier = Modifier,
    textBlock: TextBlock,
    onTextSelected: (String) -> Unit,
    index: Int,
    showSingleWordSelection: Boolean,
) {
    fun filterText(textBlock: TextBlock): String {
        return textBlock.text.lines().filter { it.isNotBlank() }.joinToString(" ")
    }
    Card(modifier = modifier) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (showSingleWordSelection){
                FlowRow(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    textBlock.lines.forEach { line ->
                        line.words.forEach {
                            SingleWorldSelection(
                                text = it,
                                onSelected = onTextSelected,
                            )
                        }
                    }
                }
            } else {
                TextBlockSelection(
                    text= filterText(textBlock),
                    onSelected = onTextSelected,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun TextScanResultCardPreview() {
    TextScanResultCard(
        textBlock = TextBlock("Example selection", emptyList(), emptyList()),
        onTextSelected = {},
        index = 0,
        showSingleWordSelection = false,
    )
}
@Preview
@Composable
fun TextScanResultCardSingleWordSelectionPreview() {
    TextScanResultCard(
        textBlock = TextBlock("Example selection", listOf(TextLine(words = listOf("Hello", "World"))), emptyList()),
        onTextSelected = {},
        index = 0,
        showSingleWordSelection = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWorldSelection(
    text: String,
    onSelected: (String) -> Unit,
) {
    val isSelected = remember { mutableStateOf(false) }
    InputChip(
        selected = isSelected.value,
        onClick = {
            isSelected.value = !isSelected.value
            onSelected(text)
          },
        label = { Text(text) }
    )
}

@Preview
@Composable
fun SingleWorldSelectionPreview() {
    SingleWorldSelection(
        text = "Example selection",
        onSelected = {},
    )
}

@Composable
fun TextBlockSelection(
    text: String,
    onSelected: (String) -> Unit,
    modifier: Modifier,
) {
    val isSelected = remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isSelected.value,
            onCheckedChange = {
                isSelected.value = it
                if (it) {
                    onSelected(text)
                }
            }
        )
        Text(
            text = text,
            modifier = modifier.weight(1.0f)
        )
    }
}

@Composable
@Preview
fun TextBlockSelectionPreview() {
    TextBlockSelection(
        text= "Example selection",
        onSelected = {},
        modifier = Modifier,
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoPermissionScreen(
    modifier: Modifier = Modifier,
    permissionStatus: PermissionStatus,
    onPermissionRequested: () -> Unit,
) {

    val openPermissionDialog = remember { mutableStateOf(false) }
    Scaffold(content = {
        // TODO Back arrow
        Column(
            modifier = modifier
                .padding(paddingValues = it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Camera permission is needed to scan text.", modifier = modifier.padding(8.dp))
            Button(
                onClick = { openPermissionDialog.value = true },
                modifier.padding(8.dp)
            ) {
                Text("Set permissions")
            }
        }
    })


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

    val configuratin = LocalConfiguration.current
    val density = LocalDensity.current
    fun drawRectangles(
        drawScope: ContentDrawScope,
        screenSize: Size,
        imageSize: Size,
        recognizedText: List<TextBlock>,
    ) {
        recognizedText.forEach { textBlock ->

            val points = textBlock.points.map { adjustPoint(MyPoint(it.x, it.y), imageSize, screenSize, uiState.rotation) }

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
                            val displayWidth = with(density) {
                                configuratin.screenWidthDp.dp.roundToPx()
                            }
                            val displayHeight = with(density) {
                                configuratin.screenHeightDp.dp.roundToPx()
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
                                onStartTextRecognition(
                                    context,
                                    cameraController,
                                    lifecycleOwner,
                                    previewView
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}