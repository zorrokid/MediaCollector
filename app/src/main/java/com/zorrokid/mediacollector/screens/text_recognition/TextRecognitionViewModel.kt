package com.zorrokid.mediacollector.screens.text_recognition

import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    logService: LogService
) : MediaCollectorViewModel(logService) {
    fun onPermissionGranted(
        permission: String,
        isGranted: Boolean,
    ) {
        // TODO
    }
}