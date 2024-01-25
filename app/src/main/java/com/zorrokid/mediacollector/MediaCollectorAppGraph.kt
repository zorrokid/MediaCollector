package com.zorrokid.mediacollector

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.zorrokid.mediacollector.screens.add_or_edit_item.AddItemScreen
import com.zorrokid.mediacollector.screens.add_or_edit_item.AddOrEditItemViewModel
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
        MainScreen(
            openScreen = { route -> appState.navigate(route) },
            navController = appState.navController,
        )
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
            restartApp = { route -> appState.clearAndNavigate(route) },
            navController = appState.navController,
        )
    }

    // TODO: could be probably cleaned up now since shared view model is not needed anymore
   navigation(
        route = "${MediaCollectorScreen.AddOrEditItem.name}$ID_ARG",
        startDestination = MediaCollectorScreen.AddOrEditItemForm.name,
        arguments = listOf(navArgument(ID) {
                nullable = true
                defaultValue = null
            })
    ){
        composable(
            route = MediaCollectorScreen.AddOrEditItemForm.name,
            arguments = listOf(navArgument(ID) {
                    nullable = true
                    defaultValue = null
                })
        ){
            val parentEntry = remember(it){
                appState.navController.getBackStackEntry(MediaCollectorScreen.AddOrEditItem.name)
            }
            val parentViewModel = hiltViewModel<AddOrEditItemViewModel>(parentEntry)
            val id = parentEntry.arguments?.getString(ID)
            parentViewModel.uiState.value = parentViewModel.uiState.value.copy(id = id ?: "")
            AddItemScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                viewModel = parentViewModel,
                popUp = appState::popUp
            )
        }
    }
    composable(route = MediaCollectorScreen.Search.name){
        SearchScreen(
            openScreen = { route -> appState.navigate(route) },
            navController = appState.navController,
        )
    }
}