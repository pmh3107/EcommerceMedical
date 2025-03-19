package app.ecommercemedical.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class WishListProduct(val productId: String, var quantity: Int)
data class WishList(val userId: String, val items: List<WishListProduct?>)
data class OrderProduct(
    val productId: String,
    val quantity: Int
)

data class OrderItem(
    @Exclude val orderId: String? = null,
    val userId: String,
    val orderDate: Timestamp,
    val products: List<OrderProduct>,
    val status: String = "pending",
    val shippingAddress: String
)