package com.zorrokid.mediacollector.screens.add_or_edit_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.BasicTopAppBar
import com.zorrokid.mediacollector.common.composable.DropDownWithTextField
import com.zorrokid.mediacollector.common.composable.TextRecognitionInput
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

@Composable
fun AddItemScreen(
    viewModel: AddOrEditItemViewModel,
    openAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit,
    popUp: () -> Unit,
) {
    val uiState by viewModel.uiState

    AddItemScreenContent(
        uiState = uiState,
        onSubmitClick = { viewModel.onSubmitClick(openAndPopUp) },
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        onReleaseAreaSelect = viewModel::onReleaseAreaSelect,
        releaseAreas = viewModel.releaseAreas,
        onConditionClassificationSelect = viewModel::onConditionClassificationSelect,
        conditionClassifications = viewModel.conditionClassifications,
        onScanText = { viewModel.onScanText(navigate) },
        onNameChange = viewModel::onNameChange,
        popUp = popUp,
        isEditing = uiState.id != null,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreenContent(
    modifier: Modifier = Modifier,
    uiState: AddOrEditItemUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    onReleaseAreaSelect: (ReleaseArea) -> Unit,
    releaseAreas: List<ReleaseArea>,
    onConditionClassificationSelect: (ConditionClassification) -> Unit,
    conditionClassifications: List<ConditionClassification>,
    onScanText: () -> Unit,
    onNameChange: (String) -> Unit,
    popUp: () -> Unit,
    isEditing: Boolean = false,
) {
   Scaffold (
       topBar = {
           val titleResourceId = if (isEditing)
               R.string.edit_item
           else
               R.string.add_item
           BasicTopAppBar(titleResourceId = titleResourceId, popUp = popUp)
        } ,
       floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Check, "Add")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                TextRecognitionInput(
                    onTextChange = onNameChange,
                    onScanText = onScanText,
                    text = uiState.name,
                    placeHolder = R.string.name,
                )
               BarcodeInput(
                    onBarcodeChange = onBarcodeChange,
                    onScanBarcodeClick = onScanBarcodeClick,
                    barcode = uiState.barcode
                )
               DropDownWithTextField(
                    onSelect = onReleaseAreaSelect,
                    selected = releaseAreas.find { it.id == uiState.releaseAreaId },
                    items = releaseAreas,
                    label = "Release area"
                )
                DropDownWithTextField(
                    onSelect = onConditionClassificationSelect,
                    selected = conditionClassifications.find { it.id == uiState.conditionClassificationId },
                    items = conditionClassifications,
                    label = "Condition"
                )
            }
        }
    )
}

@Composable
@Preview
fun AddItemScreenContentPreview(){
    AddItemScreenContent(
        uiState = AddOrEditItemUiState(),
        onSubmitClick = {},
        onBarcodeChange = {},
        onScanBarcodeClick = {},
        onReleaseAreaSelect = {},
        releaseAreas = listOf(ReleaseArea("123", "Test")),
        onConditionClassificationSelect = {},
        conditionClassifications = listOf(ConditionClassification("Test")),
        onScanText = {},
        onNameChange = {},
        popUp = {},
    )
}

