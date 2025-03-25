import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.ecommercemedical.viewmodel.AddressViewModel
import app.ecommercemedical.viewmodel.UserViewModel


@Composable
fun AddressScreen(uid: String, setUpdateAddress: () -> Unit) {
    val context = LocalContext.current
    val addressViewModel: AddressViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    var houseNumber by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var ward by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    val formattedAddress by addressViewModel.formattedAddress.collectAsState()

    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Result: ${formattedAddress ?: "No address found!"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier) {
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House Number") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Street") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier) {
            OutlinedTextField(
                value = ward,
                onValueChange = { ward = it },
                label = { Text("Ward") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedTextField(
            value = province,
            onValueChange = { province = it },
            label = { Text("Province") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val fullAddress = "$houseNumber $street, $ward, $city, $province"
                    addressViewModel.getFormattedAddress(context, fullAddress)
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Get Address")
            }
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedButton(
                enabled = formattedAddress?.isNotEmpty() ?: false,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    userViewModel.updateAddress(
                        uid = uid,
                        newAddress = formattedAddress ?: " "
                    )
                    setUpdateAddress()
                }) {
                Text("Save new address")
            }
        }
    }
}