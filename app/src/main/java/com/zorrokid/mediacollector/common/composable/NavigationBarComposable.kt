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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zorrokid.mediacollector.MediaCollectorScreen
import com.zorrokid.mediacollector.model.NavigationItem

val mainNavigationItems = listOf(
    NavigationItem(MediaCollectorScreen.Main.name, Icons.Filled.Star, "Main"),
    NavigationItem(MediaCollectorScreen.Search.name, Icons.Filled.Search, "Search"),
    NavigationItem(MediaCollectorScreen.Settings.name, Icons.Filled.Settings, "Settings"),
)

@Composable
fun MainNavigationBar(
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
   NavigationBar{
        mainNavigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
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