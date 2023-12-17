package com.zorrokid.mediacollector.screens.main

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
import androidx.navigation.NavHostController
import com.zorrokid.mediacollector.common.composable.ItemList
import com.zorrokid.mediacollector.common.composable.MainNavigationBar
import com.zorrokid.mediacollector.model.CollectionItem

@Composable
fun MainScreen(
    openScreen: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
    ) {
    val collectionItems = viewModel.collectionItems.collectAsStateWithLifecycle(emptyList())
    MainScreenContent(
        onSettingsClick = viewModel::onSettingsClick,
        openScreen = openScreen,
        collectionItems = collectionItems.value,
        onAddItemClick = viewModel::onAddItemClick,
        onEditItemClick = viewModel::onEditItemClick,
        onDeleteItemClick = viewModel::onDeleteItemClick,
        navController = navController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onSettingsClick: ((String) -> Unit) -> Unit,
    onAddItemClick: ((String) -> Unit) -> Unit,
    onEditItemClick: ((String) -> Unit, id: String) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    openScreen: (String) -> Unit,
    collectionItems: List<CollectionItem>,
    navController: NavHostController,
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
                ItemList(
                    collectionItems = collectionItems,
                    onEdit = onEditItemClick,
                    onDelete = onDeleteItemClick,
                    openScreen = openScreen
                )
            }
        },
        bottomBar = {
           MainNavigationBar(navController)
        }
    )
}

