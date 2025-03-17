package app.ecommercemedical.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavBar(
    navHostController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val items = listOf(
        ButtonNavbarItem.product,
        ButtonNavbarItem.home,
        ButtonNavbarItem.profile
    )

    BottomNavigation(
        modifier = Modifier.height(80.dp),
        backgroundColor = Color.White
    ) {
        val currentRoute =
            navHostController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                
                label = {
                    Text(
                        text = item.title,
                        modifier = Modifier
//                        color = MaterialTheme.colorScheme.primary
                    )
                },
//                selectedContentColor = MaterialTheme.colorScheme.primary,
//                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                alwaysShowLabel = false
            )
        }
    }
}