package app.ecommercemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import app.ecommercemedical.ui.MyApp
import app.ecommercemedical.ui.theme.ECommerceMedicalTheme

class App : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        val db = Firebase.firestore
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT).show()
//            }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceMedicalTheme(dynamicColor = true) {
                MyApp(modifier = Modifier)
            }
        }
    }
}
