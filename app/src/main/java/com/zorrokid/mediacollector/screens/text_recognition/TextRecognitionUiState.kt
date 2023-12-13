package com.zorrokid.mediacollector.screens.text_recognition

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.google.mlkit.vision.text.Text

data class TextRecognitionUiState(
    val recognizedText: Text = Text("", emptyList<String>()),
    val pointerOffset: Offset = Offset.Zero,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
)
