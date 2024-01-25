package com.zorrokid.mediacollector.common.text_recognition.model

import androidx.compose.ui.geometry.Size
import com.zorrokid.mediacollector.model.TextBlock

data class TextRecognitionStatus(
    val rotation: Int = 0,
    val imageSize: Size = Size(0.0f, 0.0f),
    val recognizedText: List<TextBlock> = emptyList(),
)
