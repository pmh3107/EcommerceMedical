package app.ecommercemedical.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressViewModel : ViewModel() {
    private val _formattedAddress = MutableStateFlow<String?>(null)
    val formattedAddress: StateFlow<String?> = _formattedAddress

    fun getFormattedAddress(context: Context, addressString: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(context)
                    val addresses = geocoder.getFromLocationName(addressString, 1)
                    if (!addresses.isNullOrEmpty()) {
                        addresses[0].getAddressLine(0)
                    } else {
                        "Address not found"
                    }
                } catch (e: Exception) {
                    "Error retrieving address: ${e.message}"
                }
            }
            _formattedAddress.value = result
        }
    }
}