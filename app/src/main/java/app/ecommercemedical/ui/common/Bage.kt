package app.ecommercemedical.ui.common

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.UserViewModel

@Composable
fun BadgeButton(onNavigateCheckout: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val uid by authViewModel.userID.observeAsState()
    val wishlist by orderViewModel.wishList.observeAsState()
    val wishListId by userViewModel.wishList.observeAsState()
    var itemCount by remember { mutableIntStateOf(0) }
    var badgeScale by remember { mutableFloatStateOf(1.0f) }

    LaunchedEffect(wishListId) {
        if (uid.toString().isNotEmpty()) {
            userViewModel.loadUserInfo(uid.toString())
        }
        orderViewModel.loadWishList(wishListId.toString())
    }
    itemCount = wishlist?.items?.size ?: 0

    LaunchedEffect(itemCount) {
        if (itemCount > 0) {
            animate(1.0f, 1.5f, animationSpec = tween(200)) { value, _ ->
                badgeScale = value
            }
            animate(1.5f, 1.0f, animationSpec = tween(150)) { value, _ ->
                badgeScale = value
            }
        }
    }

    BadgedBox(
        modifier = Modifier,
        badge = {
            if (itemCount > 0) {
                Badge(
                    modifier = Modifier.scale(badgeScale),
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("$itemCount")
                }
            }
        }
    ) {
        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = { onNavigateCheckout() },
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Shopping cart",
            )
        }
    }
}