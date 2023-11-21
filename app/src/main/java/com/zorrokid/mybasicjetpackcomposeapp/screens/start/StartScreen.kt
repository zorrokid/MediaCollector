package com.zorrokid.mybasicjetpackcomposeapp.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zorrokid.mybasicjetpackcomposeapp.ui.theme.MyBasicJetpackComposeAppTheme

@Composable
fun StartScreen(onLoginButtonClicked: () -> Unit, modifier: Modifier = Modifier,) {
    StartScreenContent(modifier, onLoginButtonClicked)
}

@Composable
fun StartScreenContent(
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LogInButton(onClick = onLoginButtonClicked)
    }
}

@Composable
fun LogInButton(onClick: () -> Unit) {
    Button(onClick = onClick){
        Text(text = "Log in")
    }
}

@Composable
@Preview
fun LoginButtonPreview(){
    MyBasicJetpackComposeAppTheme {
        LogInButton(onClick = { /*TODO*/ })
    }
}

