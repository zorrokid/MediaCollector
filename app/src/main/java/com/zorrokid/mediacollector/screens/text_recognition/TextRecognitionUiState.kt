package com.zorrokid.mediacollector.screens.text_recognition

import androidx.compose.ui.geometry.Size
import com.google.mlkit.vision.text.Text
import com.zorrokid.mediacollector.model.TextBlock

data class TextRecognitionUiState(
    val recognizedText: List<TextBlock> = emptyList(),
    val imageSize: Size = Size(0.0f, 0.0f),
    val rotation: Int = 0,
    val recognitionDone : Boolean = false,
)
