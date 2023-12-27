package com.zorrokid.mediacollector.model

import androidx.compose.ui.geometry.Offset

data class TextLine(val words: List<String>)
data class TextBlock(val text: String, val lines: List<TextLine>, val points: List<Offset>)
