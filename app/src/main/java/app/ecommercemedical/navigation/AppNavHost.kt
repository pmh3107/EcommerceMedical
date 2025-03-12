package app.ecommercemedical.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.ecommercemedical.ui.screens.auth.LoginScreen
import app.ecommercemedical.ui.screens.auth.Profile
import app.ecommercemedical.ui.screens.home.Home
import app.ecommercemedical.ui.screens.product.Product

//@Composable
@Composable
fun AppNavHost(modifier: Modifier = Modifier,navController: NavHostController) {
    NavHost(navController = navController, startDestination = LogIn.route) {
        composable(LogIn.route){
            LoginScreen(onLoginSuccess = { navController.navigate(Home.route)})
        }
        composable(Home.route) {
            Home()
        }
        composable(Profile.route) {
            Profile()
        }
        composable(Product.route) {
            Product()
        }
    }
}