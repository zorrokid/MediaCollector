package com.zorrokid.mediacollector.screens.add_or_edit_item

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.zorrokid.mediacollector.ID
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.ext.capitalizeWords
import com.zorrokid.mediacollector.common.snackbar.SnackbarManager
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
import kotlinx.coroutines.flow.collect
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
                    val results = storageService.collectionItems.map {
                        items ->  items.filter { item -> item.barcode == barcode }
                    }.collect{collectionItems ->
                        if (collectionItems.isNotEmpty()) {
                            uiState.value = uiState.value.copy(searchResults = collectionItems)
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
        // TODO
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

    fun onTextSelected(textList: List<String>, popUp: () -> Unit) {
        val text = textList.joinToString(" ").capitalizeWords()
        uiState.value = uiState.value.copy(name = text)
        popUp()
    }

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        val isUpdate = !uiState.value.id.isNullOrEmpty()
        val collectionItem = CollectionItem(
            id = uiState.value.id,
            name = name,
            barcode = barcode,
            userId = accountService.currentUserId,
            releaseAreaId = releaseAreaId,
            releaseAreaName = releaseAreas.find { it.id == releaseAreaId }?.name ?: "",
            conditionClassificationId = conditionClassificationId,
            conditionClassificationName = conditionClassifications.find { it.id == conditionClassificationId }?.name ?: "",
        )

        launchCatching {
            if (isUpdate) {
                storageService.update(collectionItem)
            } else {
                storageService.save(collectionItem)
            }
            var messageId = if (isUpdate) R.string.item_updated else R.string.item_added
            SnackbarManager.showMessage(messageId)
            openAndPopUp(MediaCollectorScreen.Main.name, MediaCollectorScreen.AddOrEditItemForm.name)
        }
    }
}