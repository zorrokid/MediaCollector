package com.zorrokid.mediacollector.screens.search

import com.zorrokid.mediacollector.model.CollectionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val barcode: String = "",
    val searchResults: Flow<List<CollectionItem>> = emptyFlow()
)
