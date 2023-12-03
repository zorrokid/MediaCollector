package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import com.zorrokid.mybasicjetpackcomposeapp.model.ConditionClassification
import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseArea

data class AddItemUiState(
    val barcode: String = "",
    val releaseArea: ReleaseArea = ReleaseArea(),
    val conditionClassification: ConditionClassification = ConditionClassification(),
)
