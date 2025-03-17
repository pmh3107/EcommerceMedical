package app.ecommercemedical.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ButtonNavbarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    data object home : ButtonNavbarItem(
        title = "Home",
        icon = Icons.Rounded.Home,
        route = Home.route
    )

    data object profile : ButtonNavbarItem(
        title = "Profile",
        icon = Icons.Rounded.AccountBox,
        route = Profile.route
    )

    data object product : ButtonNavbarItem(
        title = "About Store",
        icon = Icons.Rounded.Star,
        route = About.route
    )
}