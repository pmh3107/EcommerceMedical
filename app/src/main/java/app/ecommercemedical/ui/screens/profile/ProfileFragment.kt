package app.ecommercemedical.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.navigation.Home
import app.ecommercemedical.navigation.LogIn
import app.ecommercemedical.ui.dataUI.UserInfo
import app.ecommercemedical.ui.dataUI.dummyData
import app.ecommercemedical.viewmodel.AuthState
import app.ecommercemedical.viewmodel.AuthViewModel
import coil.compose.AsyncImage


@Composable
fun Profile(
    modifier: Modifier, navController: NavController, authViewModel: AuthViewModel,
    initialProfile: UserInfo = dummyData,
) {
    var imageUrl by remember { mutableStateOf(initialProfile.imageUrl) }
    var firstName by remember { mutableStateOf(initialProfile.firstName) }
    var lastName by remember { mutableStateOf(initialProfile.lastName) }
    var address by remember { mutableStateOf(initialProfile.address) }


    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    text = "Profile Information",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.avatar_default),
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(100.dp))
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Image URL") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { })
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("First Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { })
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Last Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { })
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Address") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { })
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val updatedProfile = UserInfo(
                                id = initialProfile.id,
                                imageUrl = imageUrl,
                                firstName = firstName,
                                lastName = lastName,
                                address = address
                            )
                            println("updatedProfile: $updatedProfile")
                            // handle late
                        },

                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Save Profile")
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            authViewModel.signout()
                            navController.navigate(LogIn.route)
                        },
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(text = "LogOut")
                    }
                }
            }
        }
    }
}
