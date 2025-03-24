package app.ecommercemedical.navigation

import AddressScreen
import androidx.compose.runtime.Composable
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
import app.ecommercemedical.ui.screens.cart.CartScreen
import app.ecommercemedical.ui.screens.chat.ChatScreen
import app.ecommercemedical.ui.screens.flash.FlashScreen
import app.ecommercemedical.ui.screens.home.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.ui.screens.map.MapScreen
import app.ecommercemedical.ui.screens.order.OrderScreen
import app.ecommercemedical.ui.screens.product.ProductDetailFragment
import app.ecommercemedical.ui.screens.product.ProductList
import app.ecommercemedical.viewmodel.AuthViewModel

//@Composable
@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.observeAsState()
    println("CHECK AUTH STATE: ${authState.toString()}")
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
//            }
//
//            else -> {
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

        composable(About.route) {
            AboutStore(modifier, navController, authViewModel)
        }
        composable(MapGG.route) {
            MapScreen(modifier, navController, authViewModel)
        }
        composable(Chat.route) {
            ChatScreen(modifier, navController, authViewModel)
        }
        composable(Cart.route) {
            CartScreen(modifier, navController, authViewModel)
        }
        composable(Orders.route) {
            OrderScreen(modifier, navController, authViewModel)
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
        composable(
            route = Product.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            if (categoryId != null) {
                ProductList(categoryId, modifier, navController, authViewModel)
            }
        }
    }
}