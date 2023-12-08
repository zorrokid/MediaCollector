package com.zorrokid.mediacollector

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zorrokid.mediacollector.screens.add_item.AddItemScreen
import com.zorrokid.mediacollector.screens.edit_item.EditItemScreen
import com.zorrokid.mediacollector.screens.login.LogInScreen
import com.zorrokid.mediacollector.screens.main.MainScreen
import com.zorrokid.mediacollector.screens.search.SearchScreen
import com.zorrokid.mediacollector.screens.settings.SettingsScreen
import com.zorrokid.mediacollector.screens.signup.SignUpScreen
import com.zorrokid.mediacollector.screens.splash.SplashScreen
import com.zorrokid.mediacollector.screens.start.StartScreen

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
        AddItemScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MyBasicJetpackComposeScreen.Search.name){
        SearchScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
    composable(
        route = "${MyBasicJetpackComposeScreen.EditItem.name}$ID_ARG",
        arguments = listOf(navArgument(ID) {
                nullable = true
                defaultValue = null
            })
    ){
        EditItemScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
}