package com.zorrokid.mediacollector.screens.text_recognition

import com.google.mlkit.vision.text.Text

data class TextRecognitionUiState(
    val recognizedText: Text = Text("", emptyList<String>()),
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val recognitionDone : Boolean = false,
)
