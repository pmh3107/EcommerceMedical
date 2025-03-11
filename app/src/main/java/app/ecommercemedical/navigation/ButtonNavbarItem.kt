package app.ecommercemedical.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ButtonNavbarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    data object home : ButtonNavbarItem(
        title = "Home",
        icon = Icons.Rounded.Home,
        route = app.ecommercemedical.navigation.Home.route
    )

    data object profile : ButtonNavbarItem(
        title = "Profile",
        icon = Icons.Rounded.AccountCircle,
        route = app.ecommercemedical.navigation.Profile.route
    )
}