package com.zorrokid.mediacollector.screens.main

import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.model.service.StorageService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logService: LogService,
    storageService: StorageService
) : MediaCollectorViewModel(logService){
    val collectionItems = storageService.collectionItems
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.Settings.name)
    fun onAddItemClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.AddItem.name)
    fun onEditItemClick(openScreen: (String) -> Unit, id: String){
         openScreen("${MyBasicJetpackComposeScreen.EditItem.name}?id=$id")
    }
    fun onDeleteItemClick(id: String) { /* TODO */}
}