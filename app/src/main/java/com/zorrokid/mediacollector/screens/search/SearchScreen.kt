package com.zorrokid.mediacollector.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.ItemList
import com.zorrokid.mediacollector.common.composable.MainNavigationBar
import com.zorrokid.mediacollector.model.CollectionItem

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    openScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState
    val searchResults = uiState.searchResults.collectAsStateWithLifecycle(emptyList())
    SearchScreenContent(
        uiState = uiState,
        onSubmitClick = viewModel::onSubmitClick,
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        openScreen = openScreen,
        searchResults = searchResults.value,
        onDeleteClicked = viewModel::onDeleteItemClick,
        onEditClicked = viewModel::onEditItemClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    openScreen: (String) -> Unit,
    searchResults: List<CollectionItem>,
    onEditClicked: ((String) -> Unit, id: String) -> Unit,
    onDeleteClicked: (String) -> Unit,
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Search, "Search")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                BarcodeInput(
                    onBarcodeChange = onBarcodeChange,
                    onScanBarcodeClick = onScanBarcodeClick,
                    barcode = uiState.barcode,
                )
                ItemList(
                    collectionItems = searchResults,
                    onEdit = onEditClicked,
                    onDelete = onDeleteClicked,
                    openScreen = openScreen,
                )
            }
        },
        bottomBar = {
            MainNavigationBar(openScreen)
        }
    )

}