package com.zorrokid.mediacollector.screens.add_item

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.model.CollectionItem
import com.zorrokid.mediacollector.model.ConditionClassification
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
    val releaseAreas = mutableListOf<ReleaseArea>()
    val conditionClassifications = mutableListOf<ConditionClassification>()

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
    }

    var uiState = mutableStateOf(AddItemUiState())
        private set

    private val name
        get() = uiState.value.name

    private val barcode
        get() = uiState.value.barcode

    private val releaseAreaId
        get() = uiState.value.releaseAreaId

    private val conditionClassificationId
        get() = uiState.value.conditionClassificationId

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
        uiState.value = uiState.value.copy(releaseAreaId = releaseArea.id)
    }

    fun onConditionClassificationSelect(conditionClassification: ConditionClassification) {
        uiState.value = uiState.value.copy(conditionClassificationId = conditionClassification.id)
    }

    fun onScanText(navigate: (String) -> Unit) {
        navigate(MediaCollectorScreen.TextRecognition.name)
    }

    fun onTextRecognitionResultReady(text: String) {
        uiState.value = uiState.value.copy(name = text)
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
            val collectionItem = CollectionItem(
                name = name,
                barcode = barcode,
                userId = accountService.currentUserId,
                releaseAreaId = releaseAreaId,
                releaseAreaName = releaseAreas.find { it.id == releaseAreaId }?.name ?: "",
                conditionClassificationId = conditionClassificationId,
                conditionClassificationName = conditionClassifications.find { it.id == conditionClassificationId }?.name ?: "",
            )
            storageService.save(collectionItem)
            openAndPopUp(MediaCollectorScreen.Main.name, MediaCollectorScreen.AddItem.name)
        }}
}