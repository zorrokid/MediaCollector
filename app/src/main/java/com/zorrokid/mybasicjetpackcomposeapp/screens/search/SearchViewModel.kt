package com.zorrokid.mybasicjetpackcomposeapp.screens.search

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mybasicjetpackcomposeapp.model.service.BarcodeScanService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.StorageService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    logService: LogService,
    private val barcodeScanService: BarcodeScanService,
    private val storageService: StorageService
) : MyBasicJetpackComposeAppViewModel(logService) {

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
}