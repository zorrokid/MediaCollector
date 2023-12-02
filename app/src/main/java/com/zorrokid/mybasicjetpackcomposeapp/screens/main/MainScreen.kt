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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.ItemList
import com.zorrokid.mybasicjetpackcomposeapp.common.composable.MainNavigationBar
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem

@Composable
fun MainScreen(
    openScreen: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()) {
    val collectionItems = viewModel.collectionItems.collectAsStateWithLifecycle(emptyList())
    MainScreenContent(
        onSettingsClick = viewModel::onSettingsClick,
        openScreen = openScreen,
        collectionItems = collectionItems.value,
        onAddItemClick = viewModel::onAddItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onSettingsClick: ((String) -> Unit) -> Unit,
    onAddItemClick: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit,
    collectionItems: List<CollectionItem>
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddItemClick(openScreen) },
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
                ItemList(collectionItems =  collectionItems)
            }
        },
        bottomBar = {
           MainNavigationBar(openScreen)
        }
    )
}

