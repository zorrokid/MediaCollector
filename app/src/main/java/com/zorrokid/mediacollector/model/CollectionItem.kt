package com.zorrokid.mediacollector.model

import com.google.firebase.firestore.DocumentId

/**
 * Represents a collection item.
 *
 * @property id The ID of the collection item. Id is a Firestore document ID.
 * @property name The name of the collection item.
 * @property barcode The barcode of the collection item.
 * @property userId The ID of the user who owns the collection item.
 * @property releaseAreaId The ID of the release area of the collection item.
 * @property releaseAreaName The name of the release area of the collection item. Release area is stored in this kind of denormalized manner to avoid having to do a join when querying for collection items.
 * @property collectionClassificationId The ID of the collection classification of the collection item.
 * @property collectionClassificationName The name of the collection classification of the collection item. Collection classification name is stored in this kind of denormalized manner to avoid having to do a join when querying for collection items.
 */
data class CollectionItem(
    @DocumentId val id: String = "",
    val name: String = "",
    val barcode: String = "",
    val userId: String = "",
    val releaseAreaId: String = "",
    val releaseAreaName: String = "",
    val collectionClassificationId: String = "",
    val collectionClassificationName: String = "",
)
