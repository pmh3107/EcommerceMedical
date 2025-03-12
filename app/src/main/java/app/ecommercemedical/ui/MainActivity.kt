package app.ecommercemedical.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.ecommercemedical.navigation.AppBottomNavBar
import app.ecommercemedical.navigation.AppNavHost
import app.ecommercemedical.navigation.LogIn


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: LogIn.route
    val showBottomBar = currentRoute != LogIn.route
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(
                    navHostController = navController,
                    bottomBarState = remember { mutableStateOf(true) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

