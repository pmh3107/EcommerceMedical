package app.ecommercemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import app.ecommercemedical.ui.MyApp
import app.ecommercemedical.ui.theme.ECommerceMedicalTheme
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp
class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceMedicalTheme(dynamicColor = true) {
                MyApp(modifier = Modifier)
            }
        }
    }
}
