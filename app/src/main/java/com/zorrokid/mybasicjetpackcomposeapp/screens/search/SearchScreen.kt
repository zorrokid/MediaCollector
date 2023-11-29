package com.zorrokid.mybasicjetpackcomposeapp.screens.search

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
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeField
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeScanButton
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.ItemList
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.MainNavigationBar
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem

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
        searchResults = searchResults.value
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
    searchResults: List<CollectionItem>
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Search, "Search")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                BarcodeField(uiState.barcode, onBarcodeChange, modifier)
                BarcodeScanButton(onScanBarcodeClick, modifier)
                ItemList(collectionItems = searchResults)
            }
        },
        bottomBar = {
            MainNavigationBar(openScreen)
        }
    )

}