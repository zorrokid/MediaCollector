package com.zorrokid.mediacollector.model.service

import com.zorrokid.mediacollector.model.ConditionClassification
import kotlinx.coroutines.flow.Flow

interface ConditionClassificationService {
    val conditionClassifications: Flow<List<ConditionClassification>>
}