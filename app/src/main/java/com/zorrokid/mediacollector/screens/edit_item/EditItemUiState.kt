package com.zorrokid.mediacollector.screens.edit_item

import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

data class EditItemUiState(
    val id: String = "",
    val name: String = "",
    val barcode: String = "",
    val releaseAreaId: String = "",
    val conditionClassificationId: String = "",
)
