package com.zorrokid.mediacollector.model.service

import com.zorrokid.mediacollector.model.CollectionItem
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val collectionItems: Flow<List<CollectionItem>>
    suspend fun getItem(itemId: String): CollectionItem?
    suspend fun save(task: CollectionItem): String
    suspend fun update(task: CollectionItem)
    suspend fun delete(itemId: String)
}