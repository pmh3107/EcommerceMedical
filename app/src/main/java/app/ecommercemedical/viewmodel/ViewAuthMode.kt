package app.ecommercemedical.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    private val _userID = MutableLiveData<String>()
    val userID: LiveData<String> get() = _userID

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser != null) {
            _userID.value = auth.currentUser?.uid ?: ""
            _authState.value = AuthState.Authenticated
        } else {
            _userID.value = ""
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    _userID.value = uid
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
                                                .addOnSuccessListener {
                                                    _authState.value = AuthState.Authenticated
                                                }
                                                .addOnFailureListener {
                                                    _authState.value =
                                                        AuthState.Error("Can't save wishlist ID")
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            _authState.value =
                                                AuthState.Error("Error creating wishlist: ${e.message}")
                                        }
                                } else {
                                    _authState.value = AuthState.Authenticated
                                }
                            } else {
                                val newUserInfo = UserInfo(uid, "", "", "", "", "")
                                userDocRef.set(newUserInfo)
                                    .addOnSuccessListener {
                                        _authState.value = AuthState.Authenticated
                                    }
                                    .addOnFailureListener {
                                        _authState.value = AuthState.Error("Can't create user info")
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            _authState.value =
                                AuthState.Error("Can't check user info: ${e.message}")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error occurs!")
                }
            }
    }


    fun signup(email: String, password: String, firstname: String, lastname: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    _userID.value = uid
                    val userDocRef = firestore.collection("users").document(uid)

                    val newUserInfo = UserInfo(uid, "", firstname, lastname, "", "")
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
                                        .addOnSuccessListener {
                                            _authState.value = AuthState.Authenticated
                                        }
                                        .addOnFailureListener {
                                            _authState.value =
                                                AuthState.Error("Can't save wishlist ID")
                                        }
                                }
                                .addOnFailureListener { e ->
                                    _authState.value =
                                        AuthState.Error("Error creating wishlist: ${e.message}")
                                }
                        }
                        .addOnFailureListener { e ->
                            _authState.value =
                                AuthState.Error("Can't create user info: ${e.message}")
                        }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun signout() {
        auth.signOut()
        _userID.value = ""
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
