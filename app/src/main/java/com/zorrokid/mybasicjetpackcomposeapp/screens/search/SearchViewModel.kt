package com.zorrokid.mybasicjetpackcomposeapp.screens.search

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mybasicjetpackcomposeapp.model.service.BarcodeScanService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    logService: LogService,
    private val barcodeScanService: BarcodeScanService
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

        }
}