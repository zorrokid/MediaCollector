package com.zorrokid.mediacollector.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.model.NavigationItem

val mainNavigationItems = listOf(
    NavigationItem(MediaCollectorScreen.Main.name, Icons.Filled.Star, "Main"),
    NavigationItem(MediaCollectorScreen.Search.name, Icons.Filled.Search, "Search"),
    NavigationItem(MediaCollectorScreen.Settings.name, Icons.Filled.Settings, "Settings"),
)

@Composable
fun MainNavigationBar(openScreen: (String) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }
   NavigationBar{
        mainNavigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                onClick = {
                    selectedItem = index
                    openScreen(item.route)
                },
                icon = {
                    Icon(item.icon, item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}