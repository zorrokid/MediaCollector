package com.zorrokid.mybasicjetpackcomposeapp.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem
import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.StorageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class StorageServiceImpl
    @Inject
    constructor(private val firestore: FirebaseFirestore, private val auth: AccountService):
    StorageService {
    override val collectionItems: Flow<List<CollectionItem>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(COLLECTION_ITEM_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
        }

    override suspend fun getItem(itemId: String): CollectionItem? {
        TODO("Not yet implemented")
    }

    override suspend fun save(task: CollectionItem): String {
        TODO("Not yet implemented")
    }
    override suspend fun update(task: CollectionItem) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(itemId: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COLLECTION_ITEM_COLLECTION = "collectionItems"
        private const val SAVE_COLLECTION_ITEM_TRACE = "saveCollectionItem"
        private const val UPDATE_COLLECTION_ITEM_TRACE = "updateCollectionItem"
    }
}