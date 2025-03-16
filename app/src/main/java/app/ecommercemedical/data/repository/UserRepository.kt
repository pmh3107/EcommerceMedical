package app.ecommercemedical.data.repository

import android.util.Log
import app.ecommercemedical.data.model.UserInfo
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    fun fetchUserInfo(
        uid: String,
        onSuccess: (UserInfo?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (uid.isEmpty()) {
            onSuccess(null)
            return
        }
        val userDocRef = firestore.collection("users").document(uid)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userInfo = UserInfo(
                        id = uid,
                        imageUrl = document.getString("imageUrl") ?: "",
                        firstName = document.getString("firstName") ?: "",
                        lastName = document.getString("lastName") ?: "",
                        address = document.getString("address") ?: ""
                    )
                    onSuccess(userInfo)
                } else {
                    Log.d("UserRepository", "No such document")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
