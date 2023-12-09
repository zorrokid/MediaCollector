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

fun NavGraphBuilder.mediaCollectorAppGraph(appState: MediaCollectorAppState) {
    composable(route = MediaCollectorScreen.Splash.name) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp)})
    }
    composable(route = MediaCollectorScreen.Main.name){
        MainScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(route = MediaCollectorScreen.Start.name){
        StartScreen(onLoginButtonClicked = {
            appState.navigate(MediaCollectorScreen.LogIn.name)
        })
    }
    composable(route = MediaCollectorScreen.SignUp.name){
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MediaCollectorScreen.LogIn.name){
        LogInScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MediaCollectorScreen.Settings.name){
        SettingsScreen(
            openScreen = { route -> appState.navigate(route) },
            restartApp = { route -> appState.clearAndNavigate(route) }
        )
    }
    composable(route = MediaCollectorScreen.AddItem.name){
        AddItemScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
    composable(route = MediaCollectorScreen.Search.name){
        SearchScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
    composable(
        route = "${MediaCollectorScreen.EditItem.name}$ID_ARG",
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