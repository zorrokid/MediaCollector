package com.zorrokid.mediacollector.screens.edit_item

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zorrokid.mediacollector.ID
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.snackbar.SnackbarManager
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.ReleaseArea
import com.zorrokid.mediacollector.model.service.BarcodeScanService
import com.zorrokid.mediacollector.model.service.ConditionClassificationService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.ReleaseAreaService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    // SavedStateHandle is key-value map that will let you retrieve value from the saved state with key.
    // See: https://developersbreach.com/savedstatehandle-viewmodel-android/
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val releaseAreaService: ReleaseAreaService,
    private val barcodeScanService: BarcodeScanService,
    private val conditionClassificationService: ConditionClassificationService,
) : MediaCollectorViewModel(logService) {
    val releaseAreas = mutableListOf<ReleaseArea>()
    val conditionClassifications = mutableListOf<ConditionClassification>()

    var uiState = mutableStateOf(EditItemUiState())
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

                uiState.value = EditItemUiState(
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
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onBarcodeChange(newValue: String) {
        uiState.value = uiState.value.copy(barcode = newValue)
    }

    fun onScanBarcodeClick() {
        launchCatching {
            barcodeScanService.startScanning().collect{
                if (!it.isNullOrEmpty()){
                    uiState.value = uiState.value.copy(barcode = it)
                }
            }
        }
    }

    fun onReleaseAreaSelect(releaseArea: ReleaseArea) {
        uiState.value = uiState.value.copy(
            releaseAreaId = releaseArea.id,
        )
    }

    fun onConditionClassificationSelect(conditionClassification: ConditionClassification) {
        uiState.value = uiState.value.copy(
            conditionClassificationId = conditionClassification.id,
        )
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
           storageService.update(
               CollectionItem(
               id = uiState.value.id,
               name = uiState.value.name,
               barcode = uiState.value.barcode,
               releaseAreaId = uiState.value.releaseAreaId,
               conditionClassificationId = uiState.value.conditionClassificationId,
               )
           )
            openAndPopUp(MediaCollectorScreen.Main.name, MediaCollectorScreen.EditItem.name)
            SnackbarManager.showMessage(R.string.item_updated)
        }}
}