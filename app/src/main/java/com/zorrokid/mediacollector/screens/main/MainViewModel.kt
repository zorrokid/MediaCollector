package com.zorrokid.mediacollector.screens.main

import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logService: LogService,
    storageService: StorageService
) : MyBasicJetpackComposeAppViewModel(logService){
    val collectionItems = storageService.collectionItems
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.Settings.name)
    fun onAddItemClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.AddItem.name)
}