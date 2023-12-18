package com.zorrokid.mediacollector.common.analyzers

import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.TransformExperimental
import androidx.compose.ui.geometry.Size
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.zorrokid.mediacollector.model.TextRecognitionInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * This class is responsible for detecting text from the camera preview.
 * Inspired by https://www.youtube.com/watch?v=wCADCaeS8-A&t=501s
 * and https://github.com/chouaibMo/MLKit-Jetpack-Compose
 */
class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (TextRecognitionInfo) -> Unit,
) : ImageAnalysis.Analyzer {

    companion object {
        const val THROTTLE_TIMEOUT_MS = 500L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(TransformExperimental::class, ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnSuccessListener { visionText: Text ->
                        val detectedText: String = visionText.text
                        if (detectedText.isNotBlank()) {
                            onDetectedTextUpdated(
                                TextRecognitionInfo(
                                    visionText,
                                    Size(
                                        inputImage.width.toFloat(),
                                        inputImage.height.toFloat()
                                    ),
                                    inputImage.rotationDegrees
                                )
                            )
                        }
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }

            delay(THROTTLE_TIMEOUT_MS)

        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}