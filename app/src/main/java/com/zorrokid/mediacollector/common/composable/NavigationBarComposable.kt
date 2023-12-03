package com.zorrokid.mediacollector.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen

@Composable
fun MainNavigationBar(openScreen: (String) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        MyBasicJetpackComposeScreen.Main.name,
        MyBasicJetpackComposeScreen.Search.name,
        MyBasicJetpackComposeScreen.Settings.name
    )
    NavigationBar{
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                onClick = {
                    selectedItem = index
                    openScreen(item)
                },
                icon = {
                    Icon(Icons.Filled.Add, item)
                },
                label = {
                    Text(item)
                }
            )
        }
    }
}