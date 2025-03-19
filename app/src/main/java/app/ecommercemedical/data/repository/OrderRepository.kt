package app.ecommercemedical.data.repository

import android.util.Log
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.data.model.WishList
import app.ecommercemedical.data.model.WishListProduct
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
        println("CHECK REPO ORDER ID: $orderIds")
        firestore.collection("orders")
            .whereIn("orderId", orderIds)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val orders = querySnapshot.documents.mapNotNull { document ->
                    val orderItem =
                        document.toObject(OrderItem::class.java)?.copy(orderId = document.id)
                    orderItem
                }
                onSuccess(orders)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
