package com.zorrokid.mediacollector.screens.add_or_edit_item

import com.zorrokid.mediacollector.model.CollectionItem

data class AddOrEditItemUiState(
    val id: String = "",
    val name: String = "",
    val barcode: String = "",
    val releaseAreaId: String = "",
    val conditionClassificationId: String = "",
    val searchResults: List<CollectionItem> = emptyList(),
)
