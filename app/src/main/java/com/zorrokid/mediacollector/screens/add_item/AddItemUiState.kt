package com.zorrokid.mediacollector.screens.add_item

import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

data class AddItemUiState(
    val name: String = "",
    val barcode: String = "",
    val releaseArea: ReleaseArea = ReleaseArea(),
    val conditionClassification: ConditionClassification = ConditionClassification(),
    val textRecognitionResult: String = "",
)
