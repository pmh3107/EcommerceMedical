package app.ecommercemedical.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.ecommercemedical.ui.screens.auth.LoginScreen
import app.ecommercemedical.ui.screens.auth.Profile
import app.ecommercemedical.ui.screens.auth.RegisterScreen
import app.ecommercemedical.ui.screens.home.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.ui.screens.product.Product
import app.ecommercemedical.ui.screens.product.ProductDetailFragment
import app.ecommercemedical.viewmodel.AuthViewModel

//@Composable
@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(modifier = modifier, navController = navController, startDestination = Loading.route) {
        composable(Loading.route) {
            LoadingScreen(modifier, navController, authViewModel)
        }
        composable(LogIn.route) {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable(SignUp.route) {
            RegisterScreen(modifier, navController, authViewModel)
        }
        composable(Home.route) {
            Home(modifier, navController, authViewModel)
        }
        composable(Profile.route) {
            Profile(modifier, navController, authViewModel)
        }
        composable(Product.route) {
            Product()
        }
        composable(ProductDetail.route) {
            ProductDetailFragment(modifier, navController, authViewModel)
        }
    }
}