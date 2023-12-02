package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeField
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.BarcodeScanButton
import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseArea

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit
) {
    val uiState by viewModel.uiState

    val releaseAreas = viewModel.releaseAreas.collectAsStateWithLifecycle(emptyList())

    AddItemScreenContent(
        uiState = uiState,
        onSubmitClick = { viewModel.onSubmitClick(openAndPopUp) },
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        onReleaseAreaSelect = viewModel::onReleaseAreaSelect,
        releaseAreas = releaseAreas.value,
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
    onReleaseAreaSelect: (ReleaseArea) -> Unit,
    releaseAreas: List<ReleaseArea>,
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
                ReleaseAreasListWithBox(
                    onReleaseAreaSelect = onReleaseAreaSelect,
                    uiState.releaseArea,
                    releaseAreas
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReleaseAreasListWithBox(onReleaseAreaSelect: (ReleaseArea) -> Unit, selectedArea: ReleaseArea, releaseAreas: List<ReleaseArea>) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded} ) {
        OutlinedTextField(
            value = selectedArea.name,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text("Release Area") },
            readOnly = false,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) 
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        DropdownMenu(
            onDismissRequest = { expanded = false },
            expanded = expanded,
            content = {
                releaseAreas.forEach() { releaseArea ->
                    DropdownMenuItem(
                        text = { Text(releaseArea.name) } ,
                        onClick = {
                            onReleaseAreaSelect(releaseArea)
                            expanded = false
                        }
                    )
                }},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ReleaseAreasListWithBoxPreview(){
    val releaseArea = ReleaseArea(name = "Example")
    val releaseAreas: List<ReleaseArea> = listOf(releaseArea)
    ReleaseAreasListWithBox(onReleaseAreaSelect = {}, selectedArea = releaseArea, releaseAreas = releaseAreas)
}
