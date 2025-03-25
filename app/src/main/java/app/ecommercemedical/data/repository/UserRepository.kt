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
                        address = document.getString("address") ?: "",
                        wishlist = document.getString("wishlist") ?: "",
                        orders = document.get("orders") as? List<String>
                            ?: emptyList()
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


    fun updateUserInfo(
        uid: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        updatedProfile: UserInfo,
    ) {
        if (uid.isEmpty()) {
            onError(Exception("UID is empty"))
            return
        }
        val userDocRef = firestore.collection("users").document(uid)
        userDocRef.set(updatedProfile)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }

    fun updateUserAddress(
        uid: String,
        newAddress: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        if (uid.isEmpty()) {
            onError(Exception("UID is empty"))
            return
        }
        val userDocRef = firestore.collection("users").document(uid)
        userDocRef.update("address", newAddress).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener() { error ->
            onError(error)
        }
    }
}
