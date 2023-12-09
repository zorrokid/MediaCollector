package com.zorrokid.mediacollector.screens.settings

import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
) : MediaCollectorViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(it.isAnonymous)
    }
    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(MediaCollectorScreen.LogIn.name)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(MediaCollectorScreen.SignUp.name)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(MediaCollectorScreen.Splash.name)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(MediaCollectorScreen.Splash.name)
        }
    }
}