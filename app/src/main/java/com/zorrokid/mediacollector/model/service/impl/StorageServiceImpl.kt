package com.zorrokid.mediacollector.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.StorageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
    @Inject
    constructor(private val firestore: FirebaseFirestore, private val auth: AccountService):
    StorageService {
    override val collectionItems: Flow<List<CollectionItem>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(COLLECTION_ITEM_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .dataObjects()
        }

    override suspend fun getItem(itemId: String): CollectionItem? =
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(itemId)
            .get()
            .await()
            .toObject()

    override suspend fun save(collectionItem: CollectionItem): String {
        val collectionItemWithUserId = collectionItem.copy(userId = auth.currentUserId)
        return firestore.collection(COLLECTION_ITEM_COLLECTION).add(collectionItemWithUserId).await().id
    }

    override suspend fun update(task: CollectionItem) {
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(task.id)
            .set(task)
            .await()
    }

    override suspend fun delete(itemId: String) {
        firestore.collection(COLLECTION_ITEM_COLLECTION)
            .document(itemId)
            .delete()
            .await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COLLECTION_ITEM_COLLECTION = "collectionItems"
        private const val SAVE_COLLECTION_ITEM_TRACE = "saveCollectionItem"
        private const val UPDATE_COLLECTION_ITEM_TRACE = "updateCollectionItem"
    }
}