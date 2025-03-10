package app.ecommercemedical.ui.screens.auth


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

class LoginFragment {

}

@Composable
fun LoginScreen(inLoginSuccess: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext
    Surface {
        Column {
            OutlinedTextField(userName, onValueChange = { userName = it })
            OutlinedTextField(passWord, onValueChange = { passWord = it })
        }
    }
}