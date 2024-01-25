package com.zorrokid.mediacollector.common.text_recognition.controller

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.zorrokid.mediacollector.common.text_recognition.analyzers.TextRecognitionAnalyzer
import com.zorrokid.mediacollector.model.TextRecognitionInfo

object TextRecognitionController {
    fun startTextRecognition(
        context: Context,
        cameraController: LifecycleCameraController,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onDetectedTextUpdated: (TextRecognitionInfo) -> Unit,
    ) {
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(
                onDetectedTextUpdated = {
                    onDetectedTextUpdated(it)
               },
            )
        )
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
    }

    fun onStopTextRecognition(
        cameraController: LifecycleCameraController,
        onSetSelectionFinished: () -> Unit,
    ) {
        cameraController.clearImageAnalysisAnalyzer()
        cameraController.unbind()
        onSetSelectionFinished()
    }
}