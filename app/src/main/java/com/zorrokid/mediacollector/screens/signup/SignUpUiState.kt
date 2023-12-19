package com.zorrokid.mediacollector.screens.signup

import androidx.annotation.StringRes

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val hasError: Boolean = false,
    @StringRes val errorMessageId: Int = 0,
)
