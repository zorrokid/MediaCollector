package com.zorrokid.mediacollector.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.composable.MainNavigationBar

@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState(
        initial = SettingsUiState()
    )

    SettingsScreenContent(
        uiState = uiState,
        onLoginClick = { viewModel.onLoginClick(openScreen) },
        onSignUpClick = { viewModel.onSignUpClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) },
        navController = navController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit,
    navController: NavHostController,
){
    Scaffold (
        content = { padding ->
            Column(
                modifier = modifier
                    .padding(padding)
                    .padding(bottom = 56.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (uiState.isAnonymousAccount) {
                    Button(onClick = { onLoginClick() }) {
                        Text(text = stringResource(id = R.string.login))
                    }

                    Button(onClick = { onSignUpClick() }) {
                        Text(text = stringResource(id = R.string.signup))
                    }

                } else {
                    Button(onClick = { onSignOutClick() }) {
                        Text(text = stringResource(id = R.string.sign_out))
                    }
                    Button(onClick = { onDeleteMyAccountClick() }) {
                        Text(text = stringResource(id = R.string.delete_account))
                    }
                }
            }
        },
        bottomBar = {
            MainNavigationBar(navController)
        }
    )
}
