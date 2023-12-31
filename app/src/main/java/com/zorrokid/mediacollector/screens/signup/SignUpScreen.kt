package com.zorrokid.mediacollector.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.composable.EmailField
import com.zorrokid.mediacollector.common.composable.PasswordField
import com.zorrokid.mediacollector.common.composable.RepeatPasswordField

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    // delegated / observable property, it will be recomposed when the value changes
    // https://kotlinlang.org/docs/delegated-properties.html#observable-properties
    val uiState by viewModel.uiState

    SignUpScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.hasError) {
            Text(
                stringResource(id = uiState.errorMessageId),
                modifier = Modifier.padding(8.dp)
            )
        }
        EmailField(
            value = uiState.email,
            onNewValue = onEmailChange,
            modifier = modifier
        )
        PasswordField(
            value = uiState.password,
            onNewValue = onPasswordChange,
            modifier = modifier
        )
        RepeatPasswordField(
            value = uiState.repeatPassword,
            onNewValue = onRepeatPasswordChange,
            modifier = modifier
        )
        Button(
            onClick = onSignUpClick,
            modifier = modifier
        ) {
            Text(stringResource(id = R.string.signup))
        }
    }
}
