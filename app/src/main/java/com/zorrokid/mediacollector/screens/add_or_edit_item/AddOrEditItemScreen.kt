package com.zorrokid.mediacollector.screens.add_or_edit_item

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.text_recognition.composable.CameraPreview
import com.zorrokid.mediacollector.common.composable.BarcodeInput
import com.zorrokid.mediacollector.common.composable.BasicTopAppBar
import com.zorrokid.mediacollector.common.composable.DropDownWithTextField
import com.zorrokid.mediacollector.common.composable.PermissionDialog
import com.zorrokid.mediacollector.common.composable.TextRecognitionInput
import com.zorrokid.mediacollector.common.text_recognition.composable.TextRecognitionResultSelector
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea
import com.zorrokid.mediacollector.model.TextRecognitionInfo

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddItemScreen(
    viewModel: AddOrEditItemViewModel,
    openAndPopUp: (String, String) -> Unit,
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
        onScanText = viewModel::onStartTextRecognition,
        onNameChange = viewModel::onNameChange,
        onOriginalNameChange = viewModel::onOriginalNameChange,
        popUp = popUp,
        isEditing = uiState.id != null,
        onSearchResultsDismiss = viewModel::onSearchResultsDismiss,
        onCreateCopy = viewModel::onCreateCopy,
        onTextRecognitionFinished = viewModel::onTextRecognitionFinished,
        onDetectedTextUpdated = viewModel::onDetectedTextUpdated,
        onDismissPermissionRequest = viewModel::onDismissPermissionRequest,
        onTextSelected = viewModel::onTextSelected,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
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
    onScanText: (permissionState: PermissionState, (String) -> Unit) -> Unit,
    onNameChange: (String) -> Unit,
    onOriginalNameChange: (String) -> Unit,
    popUp: () -> Unit,
    isEditing: Boolean = false,
    onSearchResultsDismiss: () -> Unit,
    onCreateCopy: (CollectionItem) -> Unit,
    onTextRecognitionFinished: () -> Unit,
    onDetectedTextUpdated: (TextRecognitionInfo) -> Unit,
    onDismissPermissionRequest: () -> Unit = {},
    onTextSelected: ((String) -> Unit, String) -> Unit,
) {
    if (uiState.addOrEditItemScreenStatus == AddOrEditItemScreenStatus.SelectTextRecognitionResults) {
        TextRecognitionResultSelector(
            modifier = modifier,
            recognizedText = uiState.textRecognitionStatus.recognizedText,
            onTextSelected = { onTextSelected(uiState.onTextSelected, it.joinToString(" "))}
        )
    }
    if (uiState.addOrEditItemScreenStatus == AddOrEditItemScreenStatus.TextRecognition) {
        CameraPreview(
            modifier = modifier,
            uiState = uiState.textRecognitionStatus,
            onTextRegognitinoFinished = onTextRecognitionFinished,
            onDetectedTextUpdated = onDetectedTextUpdated,
        )
    }
    if (uiState.addOrEditItemScreenStatus == AddOrEditItemScreenStatus.Initial) {
        FormContent(
            modifier = modifier,
            uiState = uiState,
            onSubmitClick = onSubmitClick,
            onBarcodeChange = onBarcodeChange,
            onScanBarcodeClick = onScanBarcodeClick,
            onReleaseAreaSelect = onReleaseAreaSelect,
            releaseAreas = releaseAreas,
            onConditionClassificationSelect = onConditionClassificationSelect,
            conditionClassifications = conditionClassifications,
            onScanText = onScanText,
            onNameChange = onNameChange,
            onOriginalNameChange = onOriginalNameChange,
            popUp = popUp,
            isEditing = isEditing,
            onSearchResultsDismiss = onSearchResultsDismiss,
            onCreateCopy = onCreateCopy,
            onDismissPermissionRequest = onDismissPermissionRequest,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    uiState: AddOrEditItemUiState,
    onSubmitClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    onReleaseAreaSelect: (ReleaseArea) -> Unit,
    releaseAreas: List<ReleaseArea>,
    onConditionClassificationSelect: (ConditionClassification) -> Unit,
    conditionClassifications: List<ConditionClassification>,
    onScanText: (permissionState: PermissionState, (String) -> Unit) -> Unit,
    onNameChange: (String) -> Unit,
    onOriginalNameChange: (String) -> Unit,
    popUp: () -> Unit,
    isEditing: Boolean = false,
    onSearchResultsDismiss: () -> Unit,
    onCreateCopy: (CollectionItem) -> Unit,
    onDismissPermissionRequest: () -> Unit = {},
){
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
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
                    onScanText = { onScanText(cameraPermissionState, onNameChange) },
                    text = uiState.name,
                    placeHolder = R.string.local_name,
                )
                TextRecognitionInput(
                    onTextChange = onOriginalNameChange,
                    onScanText = { onScanText(cameraPermissionState, onOriginalNameChange) },
                    text = uiState.originalName,
                    placeHolder = R.string.original_name,
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
            if (uiState.searchResults.isNotEmpty()) {
                ModalBottomSheet(
                    onDismissRequest = { onSearchResultsDismiss() }
                ) {
                    SearchResults(uiState.searchResults, onCreateCopy)
                }
            }
            if (uiState.showPermissionModal) {
                CameraPermissionModal(
                    permissionStatus = cameraPermissionState.status,
                    onPermissionRequested = {
                        cameraPermissionState.launchPermissionRequest()
                    },
                    onDismissRequest = onDismissPermissionRequest,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionModal(
    permissionStatus: PermissionStatus,
    onPermissionRequested: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openPermissionDialog = remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column {
            Text(text = "Camera permission is required to fill in field with text recognition.")
            Button(
                onClick = { openPermissionDialog.value = true },
                modifier.padding(8.dp)
            ) {
                Text("Set permissions")
            }
        }
    }
    when {
        openPermissionDialog.value -> {
            val message = if (permissionStatus.shouldShowRationale)
                "Text recognition feature requires camera permission."
            else
                "Text recognition feature requires camera permission. Please go to settings and enable camera permission."
            PermissionDialog(
                onLaunchPermissionRequest = {
                    openPermissionDialog.value = false
                    onPermissionRequested()
                },
                message = message,
                onDismiss = {
                    openPermissionDialog.value = false
                },
            )
        }
    }
}

@Composable
fun SearchResults(
    searchResults: List<CollectionItem>,
    onCreateCopy: (CollectionItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 56.dp)
    ) {
        Text(
            text = stringResource(id = R.string.create_copy_from_resul),
        )
        searchResults.forEach { collectionItem ->
           SearchResultSelect(collectionItem, onCreateCopy)
        }
    }
}

@Composable
fun SearchResultSelect(
    collectionItem: CollectionItem,
    onCreateCopy: (CollectionItem) -> Unit,
) {
    // TODO: this logic should be moved to the view model or rather
    // to CollectionItemModel (CollectionItemObject?)
    val description = if (collectionItem.name.isNotEmpty())
        collectionItem.name
    else if (collectionItem.originalName.isNotEmpty())
        collectionItem.originalName
    else
        collectionItem.barcode
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(text = description)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            onCreateCopy(collectionItem)
        }) {
            Text(text = "Create copy")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchResultSelectPreview(){
    SearchResultSelect(
        collectionItem = CollectionItem(
            name = "Test",
        ),
        onCreateCopy = {},
    )
}

/* TODO:
@OptIn(ExperimentalPermissionsApi::class)
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
        onScanText = (),
        onNameChange = {},
        onOriginalNameChange = {},
        popUp = {},
        onSearchResultsDismiss = {},
        onCreateCopy = {},
        onTextRecognitionFinished = {},
        onTextSelected = {}
    )
}*/

