package com.zorrokid.mybasicjetpackcomposeapp.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.zorrokid.mybasicjetpackcomposeapp.model.ConditionClassification
import com.zorrokid.mybasicjetpackcomposeapp.model.service.ConditionClassificationService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConditionClassificationServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ConditionClassificationService {
    override val conditionClassifications: Flow<List<ConditionClassification>>
        get() = firestore.collection(CONDITION_CLASSIFICATION_COLLECTION).dataObjects()

    companion object {
        private const val CONDITION_CLASSIFICATION_COLLECTION = "conditionClassifications"
    }
}