package com.zorrokid.mediacollector.screens.add_or_edit_item

import com.zorrokid.mediacollector.common.text_recognition.model.TextRecognitionStatus
import com.zorrokid.mediacollector.model.CollectionItem

enum class AddOrEditItemScreenStatus {
    Initial,
    TextRecognition,
    SelectTextRecognitionResults,
}

data class AddOrEditItemUiState(
    val id: String = "",
    val name: String = "",
    val originalName: String = "",
    val barcode: String = "",
    val releaseAreaId: String = "",
    val conditionClassificationId: String = "",
    val searchResults: List<CollectionItem> = emptyList(),
    val onTextSelected: (String) -> Unit = {},
    val textRecognitionStatus: TextRecognitionStatus = TextRecognitionStatus(),
    val showPermissionModal: Boolean = false,
    val addOrEditItemScreenStatus: AddOrEditItemScreenStatus = AddOrEditItemScreenStatus.Initial,
)
