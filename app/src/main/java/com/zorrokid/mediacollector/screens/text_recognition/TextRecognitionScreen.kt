package com.zorrokid.mediacollector.screens.text_recognition

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
) {
    Text("TextRecognitionScreen")
}