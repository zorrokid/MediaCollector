package com.zorrokid.mediacollector.screens.add_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.DropDownWithTextField
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit
) {
    val uiState by viewModel.uiState

    val releaseAreas = viewModel.releaseAreas.collectAsStateWithLifecycle(emptyList())
    val conditionClassifications = viewModel.conditionClassifications.collectAsStateWithLifecycle(emptyList())

    AddItemScreenContent(
        uiState = uiState,
        onSubmitClick = { viewModel.onSubmitClick(openAndPopUp) },
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        onReleaseAreaSelect = viewModel::onReleaseAreaSelect,
        releaseAreas = releaseAreas.value,
        onConditionClassificationSelect = viewModel::onConditionClassificationSelect,
        conditionClassifications = conditionClassifications.value,
        onScanText = { viewModel.onScanText(navigate) },
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
    onConditionClassificationSelect: (ConditionClassification) -> Unit,
    conditionClassifications: List<ConditionClassification>,
    onScanText: () -> Unit,
) {
   Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Check, "Add")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                BarcodeInput(
                    onBarcodeChange = onBarcodeChange,
                    onScanBarcodeClick = onScanBarcodeClick,
                    barcode = uiState.barcode
                )
               DropDownWithTextField(
                    onSelect = onReleaseAreaSelect,
                    selected = uiState.releaseArea,
                    items = releaseAreas,
                    label = "Release area"
                )
                DropDownWithTextField(
                    onSelect = onConditionClassificationSelect,
                    selected = uiState.conditionClassification,
                    items = conditionClassifications,
                    label = "Condition"
                )
                Button(onClick = onScanText) {
                    Text(text = "Scan text")
                }
            }
        }
    )
}

@Composable
@Preview
fun AddItemScreenContentPreview(){
    val selectedArea = ReleaseArea("Test")
    val uiState = AddItemUiState("12345", selectedArea)
    val releaseAreas: List<ReleaseArea> = listOf(selectedArea)
    AddItemScreenContent(
        uiState = uiState,
        onSubmitClick = {},
        onBarcodeChange = {},
        onScanBarcodeClick = {},
        onReleaseAreaSelect = {},
        releaseAreas = releaseAreas,
        onConditionClassificationSelect = {},
        conditionClassifications = listOf(ConditionClassification("Test")),
        onScanText = {},
    )
}

