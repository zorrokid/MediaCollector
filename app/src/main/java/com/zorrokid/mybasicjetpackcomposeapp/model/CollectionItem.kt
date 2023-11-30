package com.zorrokid.mybasicjetpackcomposeapp.model

import com.google.firebase.firestore.DocumentId

data class CollectionItem(
    @DocumentId val id: String = "",
    val barcode: String = "",
    val userId: String = "",
    val releaseArea: List<String> = emptyList()
)
