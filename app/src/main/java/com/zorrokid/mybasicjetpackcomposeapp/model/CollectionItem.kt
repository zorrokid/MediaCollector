package com.zorrokid.mybasicjetpackcomposeapp.model

import com.google.firebase.firestore.DocumentId

data class CollectionItem(
    @DocumentId val id: String = "",
    val barcode: String = "",
    val userId: String = "",
    // store id of denormalized data for easy updates
    val releaseAreaId: String = "",
    // denormalize are to string for easy fetch
    val releaseAreaName: String = ""
)
