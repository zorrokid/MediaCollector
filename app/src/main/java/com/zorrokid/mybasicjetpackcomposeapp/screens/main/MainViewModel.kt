package com.zorrokid.mybasicjetpackcomposeapp.screens.main

import com.zorrokid.mybasicjetpackcomposeapp.MyBasicJetpackComposeScreen
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logService: LogService
) : MyBasicJetpackComposeAppViewModel(logService){
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.Settings.name)
}