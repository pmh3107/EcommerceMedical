package app.ecommercemedical.data.repository

import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val authenticator: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkAuthStatus(onSuccess: (String?, Boolean) -> Unit, onError: (Exception) -> Unit) {
        try {
            val uid = authenticator.currentUser?.uid
            onSuccess(uid, authenticator.currentUser != null)
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onError(Exception("Email or password can't be empty"))
            return
        }

        authenticator.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = authenticator.currentUser?.uid ?: ""
                    val userDocRef = firestore.collection("users").document(uid)

                    userDocRef.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val wishlistId = document.getString("wishlist")
                                if (wishlistId.isNullOrEmpty()) {
                                    val newWishlistId =
                                        firestore.collection("wishlists").document().id
                                    val newWishlist = hashMapOf(
                                        "userId" to uid,
                                        "items" to listOf<String>()
                                    )

                                    firestore.collection("wishlists").document(newWishlistId)
                                        .set(newWishlist)
                                        .addOnSuccessListener {
                                            userDocRef.update("wishlist", newWishlistId)
                                                .addOnSuccessListener { onSuccess(authenticator.currentUser?.uid.toString()) }
                                                .addOnFailureListener { onError(it) }
                                        }
                                        .addOnFailureListener { onError(it) }
                                } else {
                                    onSuccess(uid)
                                }
                            } else {
                                val newUserInfo = UserInfo(uid, "", "", "", "", null)
                                userDocRef.set(newUserInfo)
                                    .addOnSuccessListener { onSuccess(uid) }
                                    .addOnFailureListener { onError(it) }
                            }
                        }
                        .addOnFailureListener { onError(it) }
                } else {
                    onError(Exception(task.exception))
                }
            }
    }

    fun signup(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onError(Exception("Email or password can't be empty"))
            return
        }

        authenticator.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = authenticator.currentUser?.uid ?: ""
                    val userDocRef = firestore.collection("users").document(uid)
                    val newUserInfo = UserInfo(uid, "", firstname, lastname, "", null)

                    userDocRef.set(newUserInfo)
                        .addOnSuccessListener {
                            val newWishlistId = firestore.collection("wishlists").document().id
                            val newWishlist = hashMapOf(
                                "userId" to uid,
                                "items" to listOf<String>()
                            )
                            firestore.collection("wishlists").document(newWishlistId)
                                .set(newWishlist)
                                .addOnSuccessListener {
                                    userDocRef.update("wishlist", newWishlistId)
                                        .addOnSuccessListener { onSuccess(uid) }
                                        .addOnFailureListener { onError(it) }
                                }
                                .addOnFailureListener { onError(it) }
                        }
                        .addOnFailureListener { onError(it) }
                } else {
                    onError(Exception(task.exception))
                }
            }
    }

    fun signout(
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            authenticator.signOut()
            onSuccess()
        } catch (e: Exception) {
            onError(e)
        }
    }
}