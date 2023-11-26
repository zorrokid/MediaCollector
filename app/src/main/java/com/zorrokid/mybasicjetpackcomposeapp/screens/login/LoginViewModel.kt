package com.zorrokid.mybasicjetpackcomposeapp.screens.login

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mybasicjetpackcomposeapp.common.ext.isValidEmail
import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import com.zorrokid.mybasicjetpackcomposeapp.model.service.LogService
import com.zorrokid.mybasicjetpackcomposeapp.screens.MyBasicJetpackComposeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
): MyBasicJetpackComposeAppViewModel(logService) {

    // mutableStateOf creates an observable MutableState<T>, which is an observable type integrated with the compose runtime.
    // Any changes to value schedules recomposition of any composable functions that read value.

    var uiState = mutableStateOf(LoginUiState())
    private set

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignUpClick() {
        launchCatching {
            accountService.linkAccount(email, password)
        }
    }

    fun onSignInClick() {
        if (email.isBlank() || password.isBlank()) return

        if (!email.isValidEmail()) {
            // TODO error message
            return
        }

        if (password.isBlank()) {
            // TODO error message
            return
        }
        launchCatching {
            accountService.authenticate(email, password)
        }
    }
}