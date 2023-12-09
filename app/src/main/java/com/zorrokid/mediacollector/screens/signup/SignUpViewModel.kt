package com.zorrokid.mediacollector.screens.signup

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.common.ext.isValidEmail
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MediaCollectorViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
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
    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            return
        }

        if (password.isBlank()) {
            return
        }

        if (password != uiState.value.repeatPassword) {
            return
        }

        launchCatching {
            accountService.linkAccount(email, password)
            openAndPopUp(MediaCollectorScreen.Settings.name, MediaCollectorScreen.SignUp.name)
        }
    }

}