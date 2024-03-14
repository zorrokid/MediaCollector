package com.zorrokid.mediacollector.screens.search

import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.Response

data class SearchUiState(
    val barcode: String = "",
    val collectionItemsResponse: Response<List<CollectionItem>> = Response.Initial,
)
