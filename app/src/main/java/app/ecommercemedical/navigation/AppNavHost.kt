package app.ecommercemedical.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.ecommercemedical.ui.screens.about.AboutStore
import app.ecommercemedical.ui.screens.auth.LoginScreen
import app.ecommercemedical.ui.screens.auth.Profile
import app.ecommercemedical.ui.screens.auth.RegisterScreen
import app.ecommercemedical.ui.screens.flash.FlashScreen
import app.ecommercemedical.ui.screens.home.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.ui.screens.product.ProductDetailFragment
import app.ecommercemedical.ui.screens.product.ProductList
import app.ecommercemedical.viewmodel.AuthState
import app.ecommercemedical.viewmodel.AuthViewModel

//@Composable
@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Home.route) {
                    popUpTo(Loading.route) { inclusive = true }
                }
            }

            is AuthState.Unauthenticated -> {
                navController.navigate(LogIn.route) {
                    popUpTo(Loading.route) { inclusive = true }
                }
            }

            is AuthState.Error -> {
            }

            else -> {
            }
        }
    }
    NavHost(modifier = modifier, navController = navController, startDestination = Flash.route) {
        composable(Loading.route) {
            LoadingScreen(modifier)
        }
        composable(Flash.route) {
            FlashScreen(modifier, navController, authViewModel)
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
            ProductList(modifier, navController, authViewModel)
        }
        composable(About.route) {
            AboutStore(modifier, navController, authViewModel)
        }
        composable(
            route = ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId != null) {
                ProductDetailFragment(modifier, navController, authViewModel, productId)
            }
        }
    }
}