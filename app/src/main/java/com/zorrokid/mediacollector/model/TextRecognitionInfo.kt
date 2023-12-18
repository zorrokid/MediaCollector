package com.zorrokid.mediacollector.model

import androidx.compose.ui.geometry.Size
import com.google.mlkit.vision.text.Text

data class TextRecognitionInfo(val text: Text, val imageSize: Size, val rotation: Int)
