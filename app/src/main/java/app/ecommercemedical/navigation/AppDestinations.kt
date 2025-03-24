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


object SignUp : AppDestinations {
    override val route = "signup"
}

object Loading : AppDestinations {
    override val route = "loading"
}

object Flash : AppDestinations {
    override val route = "flash"
}

object About : AppDestinations {
    override val route = "about"
}

object MapGG : AppDestinations {
    override val route = "map"
}

object Chat : AppDestinations {
    override val route = "chat"
}

object ProductDetail : AppDestinations {
    override val route = "product_detail/{productId}"
}

object Product : AppDestinations {
    override val route = "product/{categoryId}"
}


object Cart : AppDestinations {
    override val route = "cart"
}

object Orders : AppDestinations {
    override val route = "orders"
}