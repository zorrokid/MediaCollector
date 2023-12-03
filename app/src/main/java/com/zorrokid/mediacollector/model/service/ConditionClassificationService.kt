package com.zorrokid.mybasicjetpackcomposeapp.model.service

import com.zorrokid.mybasicjetpackcomposeapp.model.ConditionClassification
import kotlinx.coroutines.flow.Flow

interface ConditionClassificationService {
    val conditionClassifications: Flow<List<ConditionClassification>>
}