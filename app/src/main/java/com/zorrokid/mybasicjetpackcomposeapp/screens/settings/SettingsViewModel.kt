package com.zorrokid.mybasicjetpackcomposeapp.screens.settings

import com.zorrokid.mybasicjetpackcomposeapp.MyBasicJetpackComposeScreen
import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
) : MyBasicJetpackComposeAppViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(it.isAnonymous)
    }
    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.LogIn.name)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.SignUp.name)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(MyBasicJetpackComposeScreen.Splash.name)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(MyBasicJetpackComposeScreen.Splash.name)
        }
    }
}