package app.ecommercemedical.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BadgeButton() {
    var itemCount by remember { mutableStateOf(2) }

    BadgedBox(
        modifier = Modifier
            .height(35.dp)
            .padding(horizontal = 14.dp),
        badge = {
//            if (itemCount > 0) {
            Badge(
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Text("$itemCount")
            }
//            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "Shopping cart",
        )
    }
}