package com.zorrokid.mybasicjetpackcomposeapp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zorrokid.mybasicjetpackcomposeapp.screens.add_item.AddItemScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.login.LogInScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.main.MainScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.settings.SettingsScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.signup.SignUpScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.splash.SplashScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.start.StartScreen

fun NavGraphBuilder.myBasicJetpackComposeAppGraph(appState: MyJetpackComposeAppState) {
    composable(route = MyBasicJetpackComposeScreen.Splash.name) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp)})
    }
    composable(route = MyBasicJetpackComposeScreen.Main.name){
        MainScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(route = MyBasicJetpackComposeScreen.Start.name){
        StartScreen(onLoginButtonClicked = {
            appState.navigate(MyBasicJetpackComposeScreen.LogIn.name)
        })
    }
    composable(route = MyBasicJetpackComposeScreen.SignUp.name){
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MyBasicJetpackComposeScreen.LogIn.name){
        LogInScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MyBasicJetpackComposeScreen.Settings.name){
        SettingsScreen(
            openScreen = { route -> appState.navigate(route) },
            restartApp = { route -> appState.clearAndNavigate(route) }
        )
    }
    composable(route = MyBasicJetpackComposeScreen.AddItem.name){
        AddItemScreen()
    }
}