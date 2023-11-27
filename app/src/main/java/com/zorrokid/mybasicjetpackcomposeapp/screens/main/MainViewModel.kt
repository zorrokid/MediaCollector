package com.zorrokid.mybasicjetpackcomposeapp.screens.main

import com.zorrokid.mybasicjetpackcomposeapp.MyBasicJetpackComposeScreen
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.StorageService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logService: LogService,
    storageService: StorageService
) : MyBasicJetpackComposeAppViewModel(logService){
    val collectionItems = storageService.collectionItems
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.Settings.name)
}