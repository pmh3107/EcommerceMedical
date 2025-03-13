package app.ecommercemedical.ui.dataUI

data class UserInfo(
    val id: String,
    val imageUrl: String,
    val firstName: String,
    val lastName: String,
    val address: String
)

val dummyData = UserInfo(
    id = "@123",
    imageUrl = "https://picsum.photos/id/237/200/300",
    firstName = "John",
    lastName = "Doe",
    address = "30st Sai Gon street,"
)