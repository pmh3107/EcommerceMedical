package app.ecommercemedical.data.repository

import android.util.Log
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.data.model.OrderProduct
import app.ecommercemedical.data.model.WishList
import app.ecommercemedical.data.model.WishListProduct
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class OrderRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getWishlist(
        wishlistId: String,
        onSuccess: (WishList) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (wishlistId.isEmpty()) {
            onSuccess(WishList(userId = "", items = emptyList()))
        }
        firestore.collection("wishlists").document(wishlistId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userID = document.getString("userId") ?: ""
                    val items = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                    val wishListProduct = items.mapNotNull { item ->
                        val productId = item["productId"] as? String
                        val quantity = item["quantity"] as? Long
                        if (productId != null && quantity != null) {
                            WishListProduct(productId = productId, quantity = quantity.toInt())
                        } else {
                            null
                        }
                    }
                    val wishList = WishList(userId = userID, items = wishListProduct)
                    onSuccess(wishList)
                } else {
                    Log.d("OrderRepository", "No such document")
                    onSuccess(WishList(userId = "", items = emptyList()))
                }
            }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun uploadWishList(
        wishlistId: String,
        upLoadWishList: WishList,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (wishlistId.isEmpty()) {
            onSuccess()
        }
        firestore.collection("wishlists").document(wishlistId).set(upLoadWishList)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }

    fun removeWishList(
        wishlistId: String,
        userId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (wishlistId.isEmpty()) {
            onSuccess()
        }
        val upLoadWishList: WishList = WishList(userId = userId, items = emptyList())
        firestore.collection("wishlists").document(wishlistId).set(upLoadWishList)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }

    fun createOrder(
        orderItem: OrderItem,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("orders").add(orderItem)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun addOrderToUser(
        userId: String,
        orderId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId)
            .update("orders", FieldValue.arrayUnion(orderId))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getOrdersByIds(
        orderIds: List<String>,
        onSuccess: (List<OrderItem>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (orderIds.isEmpty()) {
            onSuccess(emptyList())
            return
        }
        val orderList: MutableList<OrderItem> = mutableListOf()
        val task = orderIds.map { orderId ->
            firestore.collection("orders").document(orderId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val orderDate: Timestamp =
                            document.get("orderDate") as? Timestamp ?: Timestamp(0, 0)
                        val products =
                            document.get("products") as? List<Map<String, Any>> ?: emptyList()
                        val listProduct = products.mapNotNull { item ->
                            val productId = item["productId"] as? String
                            val quantity = item["quantity"] as? Long
                            if (productId != null && quantity != null) {
                                OrderProduct(productId = productId, quantity = quantity.toInt())
                            } else {
                                null
                            }
                        }
                        val shippingAddress = document.get("shippingAddress") ?: ""
                        val status = document.get("status") ?: ""
                        val orderItem = OrderItem(
                            orderId = orderId,
                            orderDate = orderDate,
                            products = listProduct,
                            status = status.toString(),
                            shippingAddress = shippingAddress.toString(),
                            userId = ""
                        )
                        orderList.add(orderItem)
                    } else {
                        null
                    }
                }
        }
        Tasks.whenAllComplete(*task.toTypedArray()).addOnCompleteListener {
            onSuccess(orderList)
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }
}
