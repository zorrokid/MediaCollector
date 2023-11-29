package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeField
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeScanButton
import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseAreas.releaseAreas

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
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        onReleaseAreaSelect = viewModel::onReleaseAreaSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreenContent(
    modifier: Modifier = Modifier,
    uiState: AddItemUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    onReleaseAreaSelect: (String) -> Unit,
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
                ReleaseAreasList(onReleaseAreaSelect = onReleaseAreaSelect)
            }
        }
    )

}

// Create a composable that lists all the pre defined ReleaseAreas and user can select one of them
@Composable
fun ReleaseAreasList(onReleaseAreaSelect: (String) -> Unit) {
    // ks. https://www.youtube.com/watch?v=QCSJfMqQY9A
    var expanded by remember {
        mutableStateOf(false)
    }
    Column {
        Button(
            onClick = { expanded = true },
            content = { Text("Select Release Area") })
        DropdownMenu(
            onDismissRequest = { expanded = false },
            expanded = expanded,
            content = {
                releaseAreas.forEach() { releaseArea ->
                    DropdownMenuItem(
                        text = { Text(releaseArea.name) } ,
                        onClick = { onReleaseAreaSelect(releaseArea.name) }
                    )
                }},
        )
    }
}



