package com.zorrokid.mediacollector.screens.text_recognition

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.zorrokid.mediacollector.common.composable.PermissionDialog

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = hiltViewModel(),
) {
    TextRecognitionScreenContent()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreenContent(
    modifier: Modifier = Modifier,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    val openPermissionDialog = remember { mutableStateOf(false) }

    if (cameraPermissionState.status.isGranted) {
        Button(onClick = {}) {
            Text("Scan")
        }
    }
    else {
        Column {
            Text(text = "Camera permission is needed to scan text.")
            Button(onClick = {
                openPermissionDialog.value = true
            }) {
                Text("Permissions")
            }
        }

    }

    when {
        openPermissionDialog.value -> {
            val message = if (cameraPermissionState.status.shouldShowRationale)
                "Text recognition feature requires camera permission."
            else
                "Text recognition feature requires camera permission. Please go to settings and enable camera permission."
            PermissionDialog(
                onLaunchPermissionRequest = {
                    openPermissionDialog.value = false
                    cameraPermissionState.launchPermissionRequest()
                },
                message = message,
                onDismiss = {
                    openPermissionDialog.value = false
                },
            )
        }
    }
}