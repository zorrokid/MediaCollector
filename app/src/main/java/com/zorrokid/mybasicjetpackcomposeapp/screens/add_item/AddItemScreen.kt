package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeField
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeScanButton

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit
) {
    val uiState by viewModel.uiState

    AddItemScreenContent(
        uiState = uiState,
        onSubmitClick = { viewModel.onSubmitClick(openAndPopUp) },
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreenContent(
    modifier: Modifier = Modifier,
    uiState: AddItemUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Check, "Add")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                BarcodeField(uiState.barcode, onBarcodeChange, modifier)
                BarcodeScanButton(onScanBarcodeClick, modifier)
            }
        }
    )

}