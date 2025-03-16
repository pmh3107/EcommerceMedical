package app.ecommercemedical.navigation

interface AppDestinations {
    val route: String
}

object Home : AppDestinations {
    override val route = "home"
}

object Profile : AppDestinations {
    override val route = "profile"
}

object LogIn : AppDestinations {
    override val route = "login"
}

object Product : AppDestinations {
    override val route = "product"
}

object SignUp : AppDestinations {
    override val route = "signup"
}

object Loading : AppDestinations {
    override val route = "loading"
}

object Flash : AppDestinations {
    override val route = "flash"
}

object ProductDetail : AppDestinations {
    override val route = "product-detail"
}


