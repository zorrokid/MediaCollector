package com.zorrokid.mediacollector.screens.edit_item

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.DropDownWithTextField
import com.zorrokid.mediacollector.common.composable.FreeTextField
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea

@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit
) {
    val collectionItem by viewModel.collectionItem
    val releaseAreas = viewModel.releaseAreas.collectAsStateWithLifecycle(emptyList())
    val conditionClassifications = viewModel.conditionClassifications.collectAsStateWithLifecycle(emptyList())
    EditItemScreenContent(
        collectionItem = collectionItem,
        onSubmitClick = { viewModel.onSubmitClick(openAndPopUp) },
        onBarcodeChange = viewModel::onBarcodeChange,
        onScanBarcodeClick = viewModel::onScanBarcodeClick,
        onReleaseAreaSelect = viewModel::onReleaseAreaSelect,
        releaseAreas = releaseAreas.value,
        onConditionClassificationSelect = viewModel::onConditionClassificationSelect,
        conditionClassifications = conditionClassifications.value,
        onNameChange = viewModel::onNameChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreenContent(
    modifier: Modifier = Modifier,
    collectionItem: CollectionItem,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    onReleaseAreaSelect: (ReleaseArea) -> Unit,
    releaseAreas: List<ReleaseArea>,
    onConditionClassificationSelect: (ConditionClassification) -> Unit,
    conditionClassifications: List<ConditionClassification>,
    onNameChange: (String) -> Unit,
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onSubmitClick) {
                Icon(Icons.Filled.Check, "Update")
            }
        },
        content = { padding ->
            Column(modifier = modifier.padding(padding)){
                FreeTextField(value = collectionItem.name, onNewValue = onNameChange, placeholder = R.string.name)
                BarcodeInput(
                    onBarcodeChange = onBarcodeChange,
                    onScanBarcodeClick = onScanBarcodeClick,
                    barcode = collectionItem.barcode
                )
                DropDownWithTextField(
                    onSelect = onReleaseAreaSelect,
                    selected = releaseAreas.find {  it.id == collectionItem.releaseAreaId },
                    items = releaseAreas,
                    label = "Release area"
                )
                DropDownWithTextField(
                    onSelect = onConditionClassificationSelect,
                    selected = conditionClassifications.find { it.id == collectionItem.collectionClassificationId},
                    items = conditionClassifications,
                    label = "Condition"
                )
            }
        }
    )
}