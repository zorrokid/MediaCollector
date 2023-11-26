package com.zorrokid.mybasicjetpackcomposeapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zorrokid.mybasicjetpackcomposeapp.ui.theme.MyBasicJetpackComposeAppTheme
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBasicJetpackComposeApp (
    navController: NavHostController = rememberNavController(),
) {
    MyBasicJetpackComposeAppTheme {

        Surface {
            val appState = rememberAppState()

            Scaffold() { innerPadding ->
                NavHost(
                    navController = appState.navController,
                    startDestination = MyBasicJetpackComposeScreen.Splash.name,
                    modifier = Modifier.padding(innerPadding)
                ){
                    // setup navigation graph
                    myBasicJetpackComposeAppGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
) =
    remember(navController) {
        MyJetpackComposeAppState(navController)
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
fun MyTopAppBar(currentScreen: MyBasicJetpackComposeScreen,
                canNavigateBack: Boolean,
                navigateUp: () -> Unit,
                modifier: Modifier = Modifier) {
    TopAppBar(
       colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = stringResource(id = currentScreen.title), modifier = modifier)
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector =  Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = currentScreen.title)
                    )
                }
            }
        }
    )
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