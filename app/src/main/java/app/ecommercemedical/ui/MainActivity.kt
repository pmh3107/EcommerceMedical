package app.ecommercemedical.ui


import android.util.Log
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
import app.ecommercemedical.navigation.Cart
import app.ecommercemedical.navigation.Chat
import app.ecommercemedical.navigation.Loading
import app.ecommercemedical.navigation.LogIn
import app.ecommercemedical.navigation.SignUp
import app.ecommercemedical.viewmodel.AuthViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessaging


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: LogIn.route
    val showBottomBar =
        currentRoute != LogIn.route && currentRoute != SignUp.route && currentRoute != Loading.route && currentRoute != Chat.route && currentRoute != Cart.route
    val authViewModel: AuthViewModel = viewModel()
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }
        val token = task.result
        println("TOKEN IS HERE: $token")
    })
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

