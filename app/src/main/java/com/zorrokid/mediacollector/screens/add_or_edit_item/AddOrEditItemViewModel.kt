package com.zorrokid.mediacollector.screens.add_or_edit_item

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.SavedStateHandle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.text.Text
import com.zorrokid.mediacollector.ID
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.ext.capitalizeWords
import com.zorrokid.mediacollector.common.snackbar.SnackbarManager
import com.zorrokid.mediacollector.common.text_recognition.model.TextRecognitionStatus
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea
import com.zorrokid.mediacollector.model.TextBlock
import com.zorrokid.mediacollector.model.TextLine
import com.zorrokid.mediacollector.model.TextRecognitionInfo
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.BarcodeScanService
import com.zorrokid.mediacollector.model.service.ConditionClassificationService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.ReleaseAreaService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AddOrEditItemViewModel @Inject constructor(
    // SavedStateHandle is key-value map that will let you retrieve value from the saved state with key.
    // See: https://developersbreach.com/savedstatehandle-viewmodel-android/
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val barcodeScanService: BarcodeScanService,
    private val releaseAreaService: ReleaseAreaService,
    private val conditionClassificationService: ConditionClassificationService,
) : MediaCollectorViewModel(logService) {
    val releaseAreas = mutableListOf<ReleaseArea>()
    val conditionClassifications = mutableListOf<ConditionClassification>()

    var uiState = mutableStateOf(AddOrEditItemUiState())
        private set

    init {
        launchCatching {
            releaseAreaService.releaseAreas.collect {
                releaseAreas.addAll(it)
            }
        }
        launchCatching {
            conditionClassificationService.conditionClassifications.collect {
                conditionClassifications.addAll(it)
            }
        }
        val collectionItemId = savedStateHandle.get<String>(ID)
        if (collectionItemId != null) {
            launchCatching {
                val collectionItem = storageService.getItem(
                    collectionItemId
                ) ?: CollectionItem()

                uiState.value = AddOrEditItemUiState(
                    id = collectionItem.id,
                    name = collectionItem.name,
                    barcode = collectionItem.barcode,
                    releaseAreaId = collectionItem.releaseAreaId,
                    conditionClassificationId = collectionItem.conditionClassificationId,
                )
            }
        }
    }

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue.capitalizeWords())
    }

    fun onOriginalNameChange(newValue: String) {
        uiState.value = uiState.value.copy(originalName = newValue.capitalizeWords())
    }

    fun onBarcodeChange(newValue: String) {
        uiState.value = uiState.value.copy(barcode = newValue)
    }

    fun onScanBarcodeClick() {
        launchCatching {
            barcodeScanService.startScanning().collect{
                if (!it.isNullOrEmpty()){
                    uiState.value = uiState.value.copy(barcode = it)
                    storageService.collectionItems.map {
                        items ->  items.filter { item -> item.barcode == uiState.value.barcode}
                    }.collect{collectionItems ->
                        if (collectionItems.isNotEmpty()) {
                            uiState.value = uiState.value.copy(
                                searchResults = collectionItems,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSearchResultsDismiss() {
        uiState.value = uiState.value.copy(searchResults = emptyList())
    }

    fun onCreateCopy(collectionItem: CollectionItem) {
        uiState.value = uiState.value.copy(
            name = collectionItem.name,
            barcode = collectionItem.barcode,
            releaseAreaId = collectionItem.releaseAreaId,
            originalName = collectionItem.originalName,
            searchResults = emptyList(),
        )
    }

    fun onReleaseAreaSelect(releaseArea: ReleaseArea) {
        uiState.value = uiState.value.copy(releaseAreaId = releaseArea.id)
    }

    fun onConditionClassificationSelect(conditionClassification: ConditionClassification) {
        uiState.value = uiState.value.copy(conditionClassificationId = conditionClassification.id)
    }

    fun onDismissPermissionRequest() {
        uiState.value = uiState.value.copy(
            showPermissionModal = false,
        )
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onStartTextRecognition(permissionState: PermissionState, onTextSelected: (String) -> Unit) {
        if (permissionState.status.isGranted) {
            uiState.value = uiState.value.copy(
                addOrEditItemScreenStatus = AddOrEditItemScreenStatus.TextRecognition,
                onTextSelected = onTextSelected,
            )
        } else if (permissionState.status.shouldShowRationale.not()) {
            permissionState.launchPermissionRequest()
        } else {
            uiState.value = uiState.value.copy(
                showPermissionModal = true,
            )
        }
    }

    fun onTextSelected(onTextSelected: (String) -> Unit, text: String) {
        onTextSelected(text)
        uiState.value = uiState.value.copy(
            addOrEditItemScreenStatus = AddOrEditItemScreenStatus.Initial
        )
    }

    // TODO: move this to TextRecognitionController?
    private fun convertTextRecognitionResult(text: Text): List<TextBlock> {
        val result = text.textBlocks.map { textBlock ->
            TextBlock(
                text = textBlock.text,
                lines = textBlock.text.lines().filter {it.isNotBlank() }.map { line ->
                    TextLine(
                        words = line.split(" ").filter { it.isNotBlank() }
                    )
                },
                points = textBlock.cornerPoints?.map { Offset(it.x.toFloat(), it.y.toFloat()) } ?: emptyList()
            )
        }
        return result
    }

    // TODO: move this to TextRecognitionController?
    private fun getRotatedSize(imageSize: Size, rotation: Int): Size {
        return if (rotation == 0 || rotation == 180) {
            imageSize
        } else {
            Size(imageSize.height, imageSize.width)
        }
    }


    fun onDetectedTextUpdated(
        textRecognitionInfo: TextRecognitionInfo,
    ) {
        uiState.value = uiState.value.copy(
            textRecognitionStatus = TextRecognitionStatus(
                recognizedText = convertTextRecognitionResult(textRecognitionInfo.text),
                imageSize = getRotatedSize(textRecognitionInfo.imageSize, textRecognitionInfo.rotation),
                rotation = textRecognitionInfo.rotation,
            ),
        )
    }
    fun onTextRecognitionFinished() {
        uiState.value = uiState.value.copy(
            addOrEditItemScreenStatus = AddOrEditItemScreenStatus.SelectTextRecognitionResults
        )
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        val isUpdate = uiState.value.id.isNotEmpty()
        val collectionItem = CollectionItem(
            id = uiState.value.id,
            name = uiState.value.name,
            originalName = uiState.value.originalName,
            barcode = uiState.value.barcode,
            userId = accountService.currentUserId,
            releaseAreaId = uiState.value.releaseAreaId,
            releaseAreaName = releaseAreas.find {
                it.id == uiState.value.releaseAreaId
            }?.name ?: "",
            conditionClassificationId = uiState.value.conditionClassificationId,
            conditionClassificationName = conditionClassifications.find {
                it.id == uiState.value.conditionClassificationId
            }?.name ?: "",
        )

        launchCatching {
            if (isUpdate) {
                storageService.update(collectionItem)
            } else {
                storageService.save(collectionItem)
            }
            val messageId = if (isUpdate) R.string.item_updated else R.string.item_added
            SnackbarManager.showMessage(messageId)
            openAndPopUp(MediaCollectorScreen.Main.name, MediaCollectorScreen.AddOrEditItemForm.name)
        }
    }
}