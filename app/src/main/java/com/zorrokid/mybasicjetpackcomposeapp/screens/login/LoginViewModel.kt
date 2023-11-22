package com.zorrokid.mybasicjetpackcomposeapp.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zorrokid.mybasicjetpackcomposeapp.common.ext.isValidEmail


class LoginViewModel : ViewModel() {

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

        /* TODO authentification
        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }*/
    }

}