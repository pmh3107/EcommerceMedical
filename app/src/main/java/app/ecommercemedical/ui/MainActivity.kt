package app.ecommercemedical.ui


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.ecommercemedical.navigation.AppBottomNavBar
import app.ecommercemedical.navigation.AppNavHost
import app.ecommercemedical.navigation.Chat
import app.ecommercemedical.navigation.Checkout
import app.ecommercemedical.navigation.Loading
import app.ecommercemedical.navigation.LogIn
import app.ecommercemedical.navigation.SignUp
import app.ecommercemedical.viewmodel.AuthViewModel


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: LogIn.route
    val showBottomBar =
        currentRoute != LogIn.route && currentRoute != SignUp.route && currentRoute != Loading.route && currentRoute != Chat.route && currentRoute != Checkout.route
    val authViewModel: AuthViewModel = viewModel()
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
            modifier = Modifier.padding(innerPadding),
            authViewModel = authViewModel
        )
    }
}

