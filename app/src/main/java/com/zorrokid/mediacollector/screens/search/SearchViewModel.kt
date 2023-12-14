package com.zorrokid.mediacollector.screens.search

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.model.service.BarcodeScanService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    logService: LogService,
    private val barcodeScanService: BarcodeScanService,
    private val storageService: StorageService
) : MediaCollectorViewModel(logService) {

    var uiState = mutableStateOf(SearchUiState())
        private set

    val barcode
        get() = uiState.value.barcode

    fun onBarcodeChange(barcode: String) {
        uiState.value = uiState.value.copy(barcode = barcode)
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

    fun onSubmitClick() {
        val results = storageService.collectionItems.map {  it.filter { item -> item.barcode == barcode }}
        uiState.value = uiState.value.copy(searchResults = results)
    }

    fun onEditItemClick(openScreen: (String) -> Unit, id: String)
            = openScreen("${MediaCollectorScreen.AddOrEditItem.name}?id=$id")
    fun onDeleteItemClick(id: String) { /* TODO */}
}