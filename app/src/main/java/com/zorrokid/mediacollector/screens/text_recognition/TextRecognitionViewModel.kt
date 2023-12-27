package com.zorrokid.mediacollector.screens.text_recognition

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
import com.zorrokid.mediacollector.common.analyzers.TextRecognitionAnalyzer
import com.zorrokid.mediacollector.model.TextBlock
import com.zorrokid.mediacollector.model.TextLine
import com.zorrokid.mediacollector.model.TextRecognitionInfo
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    logService: LogService
) : MediaCollectorViewModel(logService) {

    var uiState = mutableStateOf(TextRecognitionUiState())
        private set

    private fun convertTextRecognitionResult(text: Text): List<TextBlock> {
        val result = text.textBlocks.map { textBlock ->
            TextBlock(
                text = textBlock.text,
                lines = textBlock.text.lines().filter {it.isNotBlank() }.map { line ->
                    TextLine(
                        words = line.split(" ").filter { it.isNotBlank() }
                    )
                },
                points = textBlock.cornerPoints?.map { Offset(it.x.toFloat(), it.y.toFloat()) } ?: emptyList()
            )
        }
        return result
    }

    private fun getRotatedSize(imageSize: Size, rotation: Int): Size {
        return if (rotation == 0 || rotation == 180) {
            imageSize
        } else {
            Size(imageSize.height, imageSize.width)
        }
    }

    private fun onDetectedTextUpdated(
        textRecognitionInfo: TextRecognitionInfo,
    ) {
        uiState.value = uiState.value.copy(
            recognizedText = convertTextRecognitionResult(textRecognitionInfo.text),
            imageSize = getRotatedSize(textRecognitionInfo.imageSize, textRecognitionInfo.rotation),
            rotation = textRecognitionInfo.rotation,
        )
    }
    fun startTextRecognition(
        context: Context,
        cameraController: LifecycleCameraController,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
    ) {
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(
                onDetectedTextUpdated = ::onDetectedTextUpdated,
            )
        )
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
    }

    fun onStopTextRecognition(
        cameraController: LifecycleCameraController,
    ) {
        cameraController.clearImageAnalysisAnalyzer()
        cameraController.unbind()
        uiState.value = uiState.value.copy(
            recognitionDone = true
        )
    }
}