package com.zorrokid.mediacollector.screens.text_recognition

import androidx.compose.ui.geometry.Size
import com.google.mlkit.vision.text.Text

data class TextRecognitionUiState(
    val recognizedText: Text = Text("", emptyList<String>()),
    val imageSize: Size = Size(0.0f, 0.0f),
    val rotation: Int = 0,
    val recognitionDone : Boolean = false,
)
