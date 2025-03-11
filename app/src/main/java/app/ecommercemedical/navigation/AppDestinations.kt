package app.ecommercemedical.navigation

interface AppDestinations {
    val route: String
}

object Home: AppDestinations {
    override val route = "home"
}

object Profile: AppDestinations {
    override val route = "profile"
}

object LogIn: AppDestinations {
    override val route = "login"
}

