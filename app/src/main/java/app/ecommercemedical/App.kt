package app.ecommercemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.ecommercemedical.ui.MyApp
import app.ecommercemedical.ui.screens.auth.LoginScreen
import app.ecommercemedical.ui.theme.ECommerceMedicalTheme

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceMedicalTheme {
                MyApp()
            }

        }
    }
}