package com.zorrokid.mediacollector.screens.add_item

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen
import com.zorrokid.mediacollector.model.ConditionClassification
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ReleaseArea
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.BarcodeScanService
import com.zorrokid.mediacollector.model.service.ConditionClassificationService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.ReleaseAreaService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val barcodeScanService: BarcodeScanService,
    private val releaseAreaService: ReleaseAreaService,
    private val conditionClassificationService: ConditionClassificationService,
) : MediaCollectorViewModel(logService) {
    val releaseAreas = releaseAreaService.releaseAreas
    val conditionClassifications = conditionClassificationService.conditionClassifications

    var uiState = mutableStateOf(AddItemUiState())
        private set

    private val barcode
        get() = uiState.value.barcode

    private val releaseArea
        get() = uiState.value.releaseArea

    private val collectionClassification
        get() = uiState.value.conditionClassification

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
        uiState.value = uiState.value.copy(releaseArea = releaseArea)
    }

    fun onConditionClassificationSelect(conditionClassification: ConditionClassification) {
        uiState.value = uiState.value.copy(conditionClassification = conditionClassification)
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
            val collectionItem = CollectionItem(
                barcode = barcode,
                userId = accountService.currentUserId,
                releaseAreaId = releaseArea.id,
                releaseAreaName = releaseArea.name,
                collectionClassificationId = collectionClassification.id,
                collectionClassificationName = collectionClassification.name,
            )
            storageService.save(collectionItem)
            openAndPopUp(MyBasicJetpackComposeScreen.Main.name, MyBasicJetpackComposeScreen.AddItem.name)
        }}
}