package app.ecommercemedical.data.repository

import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getWishlist(onSuccess: (List<*>) -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("wish_list")
    }
}