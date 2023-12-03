package com.zorrokid.mybasicjetpackcomposeapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zorrokid.mybasicjetpackcomposeapp.ui.theme.MyBasicJetpackComposeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBasicJetpackComposeApp (
    navController: NavHostController = rememberNavController(),
) {
    MyBasicJetpackComposeAppTheme {

        Surface {
            val appState = rememberAppState()

            Scaffold() { innerPadding ->
                NavHost(
                    navController = appState.navController,
                    startDestination = MyBasicJetpackComposeScreen.Splash.name,
                    modifier = Modifier.padding(innerPadding)
                ){
                    myBasicJetpackComposeAppGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
) =
    remember(navController) {
        MyJetpackComposeAppState(navController)
    }