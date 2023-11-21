package com.zorrokid.mybasicjetpackcomposeapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zorrokid.mybasicjetpackcomposeapp.ui.theme.MyBasicJetpackComposeAppTheme
import androidx.navigation.compose.rememberNavController
import com.zorrokid.mybasicjetpackcomposeapp.screens.login.LogInScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.main.MainScreen
import com.zorrokid.mybasicjetpackcomposeapp.screens.start.StartScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBasicJetpackComposeApp (
    navController: NavHostController = rememberNavController(),
) {
    MyBasicJetpackComposeAppTheme {
        var presses by remember { mutableStateOf(0) }
        Scaffold(
            topBar = { MyTopAppBar(text = "Top app bar") },
            bottomBar = { MyBottomAppBar(text = "Bottom app bar") },
            floatingActionButton = {
                FloatingActionButton(onClick = { presses++ }) {
                   AddIcon()
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MyBasicJetpackComposeScreen.Start.name,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(route = MyBasicJetpackComposeScreen.Main.name){
                    MainScreen()
                }
                composable(route = MyBasicJetpackComposeScreen.Start.name){
                    StartScreen()
                }
                composable(route = MyBasicJetpackComposeScreen.LogIn.name){
                    LogInScreen()
                }
            }
        }
    }
}


@Composable
fun AddIcon() {
    Icon(Icons.Default.Add, contentDescription = "Add")
}

@Composable
@Preview(showBackground = true)
fun AddIconPreview() {
    MyBasicJetpackComposeAppTheme {
        AddIcon()
    }
}

@Composable
@Preview
fun MyBasicJetpackComposeAppPreview() {
    MyBasicJetpackComposeApp()
}


@Composable
fun MainContent(presses: Int, innerPadding: PaddingValues = PaddingValues()) {

}

@Composable
@Preview(showBackground = true)
fun MainContentPreview() {
    MyBasicJetpackComposeAppTheme {
        MainContent(presses = 1)
    }
}

@Composable
fun MyBottomAppBar(text: String, modifier: Modifier = Modifier) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = text
        )
    }
}

@Composable
@Preview
fun MyBottomAppBarPreview() {
    MyBasicJetpackComposeAppTheme {
        MyBottomAppBar(text = "Bottom app bar")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(text: String, modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = text, modifier = modifier,)
        }
    )
}

@Composable
@Preview
fun MyTopAppBarPreview(){
    MyBasicJetpackComposeAppTheme {
        MyTopAppBar(text = "Top app bar")
    }
}

@Composable
fun CounterText(presses: Int, modifier: Modifier = Modifier) {
    Text(
        text = "You've pressed $presses times.",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CounterTextPreview() {
    MyBasicJetpackComposeAppTheme {
        CounterText(1)
    }
}