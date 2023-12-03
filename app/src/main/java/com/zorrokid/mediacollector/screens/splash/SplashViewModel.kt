package com.zorrokid.mediacollector.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuthException
import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    // TODO configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : MyBasicJetpackComposeAppViewModel(logService) {
    val showError = mutableStateOf(false)

    /* TODO
        init {
        launchCatching { configurationService.fetchConfiguration() }
    }*/

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(MyBasicJetpackComposeScreen.Main.name, MyBasicJetpackComposeScreen.Splash.name)
        else createAnonymousAccount(openAndPopUp)
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            openAndPopUp(MyBasicJetpackComposeScreen.Main.name, MyBasicJetpackComposeScreen.Splash.name)
        }
    }
}
