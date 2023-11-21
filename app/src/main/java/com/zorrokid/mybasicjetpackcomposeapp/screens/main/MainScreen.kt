package com.zorrokid.mybasicjetpackcomposeapp.screens.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    MainScreenContent()
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
) {
    Text("MainScreenContent")
}
