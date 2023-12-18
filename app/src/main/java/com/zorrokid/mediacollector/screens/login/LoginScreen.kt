package com.zorrokid.mediacollector.screens.login

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
import com.zorrokid.mediacollector.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.zorrokid.mediacollector.common.composable.EmailField
import com.zorrokid.mediacollector.common.composable.PasswordField

@Composable
fun LogInScreen(
/* Note: Due to their lifecycle and scoping, you should access and call ViewModel instances at
   screen-level composables, that is, close to a root composable called from an activity,
   fragment, or destination of a Navigation graph. You should never pass down ViewModel
   instances to other composables, pass only the data they need and functions that perform
   the required logic as parameters./
*/
    viewModel: LoginViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit,
){
    val uiState by viewModel.uiState
    LogInScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick =  { viewModel.onSignInClick(openAndPopUp) }
    )
}

@Composable
fun LogInScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    ) {
    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        EmailField(value = uiState.email, onNewValue = onEmailChange)
        PasswordField(value = uiState.password, onNewValue = onPasswordChange)
        Button(onClick = onSignInClick) {
            Text(stringResource(id = R.string.login))
        }
    }
}

