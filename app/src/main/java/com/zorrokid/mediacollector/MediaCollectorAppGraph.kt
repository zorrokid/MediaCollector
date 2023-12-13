package com.zorrokid.mediacollector

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.zorrokid.mediacollector.screens.add_item.AddItemScreen
import com.zorrokid.mediacollector.screens.add_item.AddItemViewModel
import com.zorrokid.mediacollector.screens.edit_item.EditItemScreen
import com.zorrokid.mediacollector.screens.login.LogInScreen
import com.zorrokid.mediacollector.screens.main.MainScreen
import com.zorrokid.mediacollector.screens.search.SearchScreen
import com.zorrokid.mediacollector.screens.settings.SettingsScreen
import com.zorrokid.mediacollector.screens.signup.SignUpScreen
import com.zorrokid.mediacollector.screens.splash.SplashScreen
import com.zorrokid.mediacollector.screens.start.StartScreen
import com.zorrokid.mediacollector.screens.text_recognition.TextRecognitionScreen

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
    navigation(
        route = MediaCollectorScreen.AddItemParent.name,
        startDestination = MediaCollectorScreen.AddItem.name,
    ){
        composable(route = MediaCollectorScreen.AddItem.name){
            val parentEntry = remember(it){
                appState.navController.getBackStackEntry(MediaCollectorScreen.AddItemParent.name)
            }
            val parentViewModel = hiltViewModel<AddItemViewModel>(parentEntry)
            AddItemScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                navigate = { route -> appState.navigate(route) },
                viewModel = parentViewModel
            )
        }
        composable(route = MediaCollectorScreen.TextRecognition.name){
            val parentEntry = remember(it){
                appState.navController.getBackStackEntry(MediaCollectorScreen.AddItemParent.name)
            }
            val parentViewModel = hiltViewModel<AddItemViewModel>(parentEntry)
            TextRecognitionScreen(
                sharedViewModel = parentViewModel,
                popUp = { appState.popUp() }
            )
        }
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