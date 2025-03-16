package app.ecommercemedical.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.ecommercemedical.ui.screens.auth.LoginScreen
import app.ecommercemedical.ui.screens.auth.Profile
import app.ecommercemedical.ui.screens.auth.RegisterScreen
import app.ecommercemedical.ui.screens.flash.FlashScreen
import app.ecommercemedical.ui.screens.home.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.ui.screens.product.Product
import app.ecommercemedical.ui.screens.product.ProductDetailFragment
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

//    LaunchedEffect(authState) {
//        when (authState) {
//            is AuthState.Authenticated -> {
//                navController.navigate(Home.route) {
//                    popUpTo(Loading.route) { inclusive = true }
//                }
//            }
//
//            is AuthState.Unauthenticated -> {
//                navController.navigate(LogIn.route) {
//                    popUpTo(Loading.route) { inclusive = true }
//                }
//            }
//
//            is AuthState.Error -> {
//                // Xử lý lỗi nếu cần
//            }
//
//            else -> {
//                // Xử lý các trạng thái khác nếu cần
//            }
//        }
//    }
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
            Product()
        }
        composable(ProductDetail.route) {
            ProductDetailFragment(modifier, navController, authViewModel)
        }
    }
}