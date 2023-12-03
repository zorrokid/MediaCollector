package com.zorrokid.mediacollector.model

import com.google.firebase.firestore.DocumentId

data class ConditionClassification(
    @DocumentId override val id: String = "",
    override val name: String = "",
    val value: Int = 0,
) : IdAndNameObject
