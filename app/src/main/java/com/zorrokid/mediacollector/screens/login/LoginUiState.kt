package com.zorrokid.mediacollector.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isInvalidCredentials: Boolean = false,
)
