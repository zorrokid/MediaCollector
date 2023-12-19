package com.zorrokid.mediacollector.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.ext.isValidEmail
import com.zorrokid.mediacollector.common.snackbar.SnackbarManager
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
): MediaCollectorViewModel(logService) {

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

    fun setInvalidCredentials(isInvalidCredentials: Boolean) {
        uiState.value = uiState.value.copy(
            isInvalidCredentials = isInvalidCredentials,
        )
    }

    fun onSignInClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        if (!email.isValidEmail() || password.isBlank()) {
            setInvalidCredentials(true)
            return
        }

        setInvalidCredentials(false)
        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(MediaCollectorScreen.Settings.name, MediaCollectorScreen.LogIn.name)
        }
    }
}