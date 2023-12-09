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
    val collectionItem = mutableStateOf(CollectionItem())
    val releaseAreas = releaseAreaService.releaseAreas
    val conditionClassifications = conditionClassificationService.conditionClassifications

    init {
        val collectionItemId = savedStateHandle.get<String>(ID)
        if (collectionItemId != null) {
            launchCatching {
                collectionItem.value = storageService.getItem(
                    collectionItemId
                ) ?: CollectionItem()
            }
        }
    }


    fun onBarcodeChange(newValue: String) {
        collectionItem.value = collectionItem.value.copy(barcode = newValue)
    }

    fun onScanBarcodeClick() {
        launchCatching {
            barcodeScanService.startScanning().collect{
                if (!it.isNullOrEmpty()){
                    collectionItem.value = collectionItem.value.copy(barcode = it)
                }
            }
        }
    }

    fun onReleaseAreaSelect(releaseArea: ReleaseArea) {
        collectionItem.value = collectionItem.value.copy(
            releaseAreaId = releaseArea.id,
            releaseAreaName = releaseArea.name
        )
    }

    fun onConditionClassificationSelect(conditionClassification: ConditionClassification) {
        collectionItem.value = collectionItem.value.copy(
            collectionClassificationId = conditionClassification.id,
            collectionClassificationName = conditionClassification.name
        )
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
           storageService.update(collectionItem.value)
            openAndPopUp(MediaCollectorScreen.Main.name, MediaCollectorScreen.AddItem.name)
            SnackbarManager.showMessage(R.string.item_updated)
        }}
}