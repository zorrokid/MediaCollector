package com.zorrokid.mediacollector.screens.text_recognition

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
import com.zorrokid.mediacollector.common.analyzers.TextRecognitionAnalyzer
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

    private val recognizedText
        get() = uiState.value.recognizedText

    private val pointerOffset
        get() = uiState.value.pointerOffset

    fun onTouchEvent(offset: Offset, cameraController: LifecycleCameraController) {
        cameraController.clearImageAnalysisAnalyzer()
        onPointerOffsetChanged(offset)
    }
    fun onPointerOffsetChanged(newOffset: Offset) {
        uiState.value = uiState.value.copy(pointerOffset = newOffset)
    }

    fun onDetectedTextUpdated(text: Text, imageWidth: Int, imageHeight: Int) {
        uiState.value = uiState.value.copy(
            recognizedText = text,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
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

    fun onStopTextRecognition(cameraController: LifecycleCameraController, onTextRecognitionResultReady: (String) -> Unit) {
        cameraController.clearImageAnalysisAnalyzer()
        cameraController.unbind()
        onTextRecognitionResultReady(recognizedText.text)
    }

    fun onPhotoCaptured(context: Context, bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}