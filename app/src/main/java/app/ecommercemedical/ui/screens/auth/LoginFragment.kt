package app.ecommercemedical.ui.screens.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.navigation.Home
import app.ecommercemedical.navigation.SignUp
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthState
import app.ecommercemedical.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsState()

    var isErrorEmail by remember {
        mutableStateOf(false)
    }
    val isErrorPassword by remember { mutableStateOf(false) }

    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> navController.navigate(Home.route)
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    "The supplied auth credential is incorrect, malformed or has expired.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AuthState.Loading -> isLoading = true
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        focusRequesterEmail.requestFocus()
    }

    if (isLoading) {
        LoadingScreen()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Welcome Back!",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 38.dp),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight(700)
                )
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isErrorEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches().not()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterEmail),
                        singleLine = true,
                        isError = isErrorEmail,
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Email:") },
                        placeholder = { Text("Enter your email address") },
                        supportingText = { if (isErrorEmail) Text("Enter a valid email address") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequesterPassword.requestFocus() }
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterPassword),
                        singleLine = true,
                        isError = isErrorPassword,
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        label = { Text("Password:") },
                        placeholder = { Text("Enter your password") },
                        supportingText = { if (isErrorPassword) Text("Password cannot be empty") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (!isErrorEmail && !isErrorPassword) {
                                    authViewModel.login(email, password)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please fix the errors",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = { authViewModel.login(email, password) },
                        enabled = password.isNotEmpty() && email.isNotEmpty()
                    ) {
                        Text(
                            "Login",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("- or continue with -", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = { /* TODO: Implement Google Sign-In */ }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier.height(20.dp),
                                painter = painterResource(R.drawable.google_icon),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                "Sign In With Google",
                                modifier = Modifier.padding(vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(onClick = { navController.navigate(SignUp.route) }) {
                        Text(
                            "Don't have an account? Sign Up",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}