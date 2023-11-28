package com.zorrokid.mybasicjetpackcomposeapp.screens.add_item

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mybasicjetpackcomposeapp.MyBasicJetpackComposeScreen
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem
import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.BarcodeScanService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.StorageService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectIndexed
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val barcodeScanService: BarcodeScanService
) : MyBasicJetpackComposeAppViewModel(logService) {
    var uiState = mutableStateOf(AddItemUiState())
        private set

    private val barcode
        get() = uiState.value.barcode

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

    fun onSubmitClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
            storageService.save(CollectionItem(barcode = barcode, userId = accountService.currentUserId ))
            openAndPopUp(MyBasicJetpackComposeScreen.Main.name, MyBasicJetpackComposeScreen.AddItem.name)
        }}
}