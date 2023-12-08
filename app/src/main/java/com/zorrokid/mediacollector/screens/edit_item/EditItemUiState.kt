package com.zorrokid.mediacollector.screens.edit_item

import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

data class EditItemUiState(
    val barcode: String = "",
    val releaseArea: ReleaseArea = ReleaseArea(),
    val conditionClassification: ConditionClassification = ConditionClassification(),
)
