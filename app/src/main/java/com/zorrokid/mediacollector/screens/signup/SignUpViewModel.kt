package com.zorrokid.mediacollector.screens.signup

import androidx.compose.runtime.mutableStateOf
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.R
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

    fun setErrorMessage(resId: Int) {
        uiState.value = uiState.value.copy(
            hasError = true,
            errorMessageId = resId
        )
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            setErrorMessage(R.string.invalid_email)
            return
        }

        if (password.isBlank()) {
            setErrorMessage(R.string.invalid_password)
            return
        }

        if (password != uiState.value.repeatPassword) {
            setErrorMessage(R.string.invalid_repeat_password)
            return
        }

        launchCatching {
            accountService.linkAccount(email, password)
            openAndPopUp(MediaCollectorScreen.Settings.name, MediaCollectorScreen.SignUp.name)
        }
    }

}