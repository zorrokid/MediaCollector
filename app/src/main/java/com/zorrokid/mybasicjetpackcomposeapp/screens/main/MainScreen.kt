package com.zorrokid.mybasicjetpackcomposeapp.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(
    openScreen: (String) -> Unit,
   viewModel: MainViewModel = hiltViewModel()) {
    MainScreenContent(onSettingsClick = viewModel::onSettingsClick, openScreen = openScreen)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onSettingsClick: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit,
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        content = { padding ->
            
            Column(modifier = modifier.padding(padding)) {
                TopAppBar(title = { Text("Main screen") }, actions = {
                    Box(modifier){
                        IconButton(onClick = { onSettingsClick(openScreen) }) {
                            Icon(Icons.Filled.Settings, "Settings")
                        }
                    }
                })
                Text(text = "Main Screen")
            }
        }
    )
}
