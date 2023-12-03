package com.zorrokid.mediacollector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint generates an individual Hilt component for each Android class in your project.
// These components can receive dependencies from their respective parent classes as described
// in Component hierarchy.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBasicJetpackComposeApp()
        }
    }
}

