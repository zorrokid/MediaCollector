package com.zorrokid.mediacollector.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.ItemList
import com.zorrokid.mediacollector.common.composable.MainNavigationBar
import com.zorrokid.mediacollector.model.Response

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    openScreen: (String) -> Unit,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState
    SearchScreenContent(
        uiState = uiState,
        onSubmitClick = viewModel::onSubmitClick,
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        openScreen = openScreen,
        onDeleteClicked = viewModel::onDeleteItemClick,
        onEditClicked = viewModel::onEditItemClick,
        navController = navController,
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    openScreen: (String) -> Unit,
    onEditClicked: ((String) -> Unit, id: String) -> Unit,
    onDeleteClicked: (String) -> Unit,
    navController: NavHostController,
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
                when (uiState.collectionItemsResponse) {
                    is Response.Initial -> {
                        Text("Search for items by barcode")
                    }
                    is Response.Loading -> {
                        Text("Seaching for results")
                    }

                    is Response.Success -> {
                        val collectionItems = uiState.collectionItemsResponse.data
                        if (collectionItems.isEmpty()) {
                            Text("No items found")
                        } else {
                            ItemList(
                                collectionItems = collectionItems,
                                onEdit = onEditClicked,
                                onDelete = onDeleteClicked,
                                openScreen = openScreen,
                            )
                        }
                    }

                    is Response.Error -> {
                        Text(uiState.collectionItemsResponse.message)
                    }
                }
            }
        },
        bottomBar = {
            MainNavigationBar(navController)
        }
    )

}