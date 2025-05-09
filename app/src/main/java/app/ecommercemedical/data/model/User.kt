package app.ecommercemedical.data.model


data class UserInfo(
    var id: String,
    var imageUrl: String,
    var firstName: String,
    var lastName: String,
    var address: String,
    var wishlist: String?,
    var orders: List<String> = emptyList()
)