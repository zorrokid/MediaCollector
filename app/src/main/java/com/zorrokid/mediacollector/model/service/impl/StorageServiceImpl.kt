package com.zorrokid.mediacollector.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.Response
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.StorageService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
    @Inject
    constructor(private val firestore: FirebaseFirestore, private val auth: AccountService):
    StorageService {
    override val collectionItems: Flow<List<CollectionItem>>
        // flatMapLatest:
        // Returns a flow that switches to a new flow produced by transform function every time the original flow emits a value.
        // When the original flow emits a new value, the previous flow produced by transform block is cancelled.
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(COLLECTION_ITEM_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .dataObjects()
        }

    override suspend fun getItem(itemId: String): CollectionItem? =
        // TODO: check that user has access to this item
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(itemId)
            .get()
            .await()
            .toObject()

    override suspend fun save(collectionItem: CollectionItem): String {
        val collectionItemWithUserId = collectionItem.copy(userId = auth.currentUserId)
        return firestore.collection(COLLECTION_ITEM_COLLECTION).add(collectionItemWithUserId).await().id
    }

    override suspend fun update(collectionItem: CollectionItem) {
        val collectionItemWithUserId = collectionItem.copy(userId = auth.currentUserId)
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(collectionItemWithUserId.id)
            .set(collectionItemWithUserId)
            .await()
    }

    override suspend fun delete(itemId: String) {
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(itemId)
            .delete()
            .await()
    }

    override suspend fun getCollectionItemsByBarcode(barcode: String): Flow<Response<List<CollectionItem>>> =
        callbackFlow {
            if (barcode.isEmpty()) {
                close()
                return@callbackFlow
            }

            val query = firestore.collection(COLLECTION_ITEM_COLLECTION)
                .whereEqualTo("barcode", barcode)
                .whereEqualTo(USER_ID_FIELD, auth.currentUserId);

            val snapshotListener = query.addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null && !snapshot.isEmpty) {
                    val collectionItem = snapshot.toObjects(CollectionItem::class.java)
                    Response.Success(collectionItem)
                } else {
                    Response.Error(e?.message ?: "No collection items found")
                }
                trySend(response).isSuccess
            }
            awaitClose {
                snapshotListener.remove()
            }
        }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COLLECTION_ITEM_COLLECTION = "collectionItems"
        private const val SAVE_COLLECTION_ITEM_TRACE = "saveCollectionItem"
        private const val UPDATE_COLLECTION_ITEM_TRACE = "updateCollectionItem"
    }
}