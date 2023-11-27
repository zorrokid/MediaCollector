package com.zorrokid.mybasicjetpackcomposeapp.model.service

import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val collectionItems: Flow<List<CollectionItem>>
    suspend fun getItem(itemId: String): CollectionItem?
    suspend fun save(task: CollectionItem): String
    suspend fun update(task: CollectionItem)
    suspend fun delete(itemId: String)
}