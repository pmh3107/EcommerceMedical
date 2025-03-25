package app.ecommercemedical.ui.screens.auth

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.navigation.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.utils.getPasswordErrorMessage
import app.ecommercemedical.utils.isEmailValid
import app.ecommercemedical.utils.isFormValid
import app.ecommercemedical.utils.isNameValid
import app.ecommercemedical.utils.isPasswordValid
import app.ecommercemedical.viewmodel.AuthState
import app.ecommercemedical.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    var isErrorFirstname by remember { mutableStateOf(false) }
    var isErrorLastname by remember { mutableStateOf(false) }
    var isErrorEmail by remember { mutableStateOf(false) }
    var isErrorPassword by remember { mutableStateOf(false) }
    var isErrorConfirmPassword by remember { mutableStateOf(false) }

    val focusRequesterFirstname = remember { FocusRequester() }
    val focusRequesterLastname = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterConfirmPassword = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Create account successfully!", Toast.LENGTH_SHORT).show()
                navController.navigate(Home.route)
            }

            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            is AuthState.Loading -> {
                isLoading = true
            }

            else -> Unit
        }
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
                    "Welcome!",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight(700)
                )
                Text(
                    "to MyApp",
                    modifier = Modifier.padding(bottom = 38.dp),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight(700)
                )
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = firstname,
                        onValueChange = {
                            firstname = it
                            isErrorFirstname = !isNameValid(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterFirstname),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("First name") },
                        isError = isErrorFirstname,
                        supportingText = {
                            if (isErrorFirstname) {
                                Text("First name must be at least 2 characters.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequesterLastname.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        value = lastname,
                        onValueChange = {
                            lastname = it
                            isErrorLastname = !isNameValid(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterLastname),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Last name") },
                        isError = isErrorLastname,
                        supportingText = {
                            if (isErrorLastname) {
                                Text("Last name must be at least 2 characters.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequesterEmail.requestFocus() }
                        )
                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isErrorEmail = !isEmailValid(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterEmail),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        label = { Text("Email") },
                        isError = isErrorEmail,
                        supportingText = {
                            if (isErrorEmail) {
                                Text("Please enter a valid email address.")
                            }
                        },
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
                        onValueChange = {
                            password = it
                            isErrorPassword = !isPasswordValid(it)
                            isErrorConfirmPassword = (it != passwordCheck)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterPassword),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorPassword,
                        supportingText = {
                            if (isErrorPassword) {
                                Text(getPasswordErrorMessage(password))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequesterConfirmPassword.requestFocus() }
                        )
                    )

                    OutlinedTextField(
                        value = passwordCheck,
                        onValueChange = {
                            passwordCheck = it
                            isErrorConfirmPassword = (password != it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterConfirmPassword),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorConfirmPassword,
                        supportingText = {
                            if (isErrorConfirmPassword) {
                                Text("Passwords do not match.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (isFormValid(
                                        isErrorFirstname,
                                        isErrorLastname,
                                        isErrorEmail,
                                        isErrorPassword,
                                        isErrorConfirmPassword
                                    )
                                ) {
                                    authViewModel.signup(email, password, firstname, lastname)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please fix the errors before submitting.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = {
                            authViewModel.signup(email, password, firstname, lastname)
                        },
                        enabled = isFormValid(
                            isErrorFirstname,
                            isErrorLastname,
                            isErrorEmail,
                            isErrorPassword,
                            isErrorConfirmPassword
                        )
                    ) {
                        Text(
                            "Register",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Optional Google Sign-In Button
                    Text("- or continue with -", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = { /* Handle Google Sign-In */ }
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
                }
            }
        }
    }
}