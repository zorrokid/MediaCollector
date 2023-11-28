package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeField
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeScanButton
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.SubmitButton

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

@Composable
fun AddItemScreenContent(
    modifier: Modifier = Modifier,
    uiState: AddItemUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit
) {
    Column {
        BarcodeField(uiState.barcode, onBarcodeChange, modifier)
        BarcodeScanButton(onScanBarcodeClick, modifier)
        SubmitButton(onSubmitClick, modifier)
    }
}