package app.ecommercemedical.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.model.UserInfo
//import app.ecommercemedical.data.model.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = if (auth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        println("CHECKLOADING: ${AuthState.Loading}")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val userDocRef = firestore.collection("users").document(uid)
                    userDocRef.get()
                        .addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val newUserInfo = UserInfo(uid, "", "", "", "")
                                userDocRef.set(newUserInfo)
                                    .addOnSuccessListener {
                                        _authState.value = AuthState.Authenticated

                                    }
                                    .addOnFailureListener {
                                        _authState.value =
                                            AuthState.Error("Can't not create info user")
                                    }
                            } else {
                                _authState.value = AuthState.Authenticated
                                println("CHECKVAR_DOCUMENT: $document")
                            }
                        }
                        .addOnFailureListener { e ->
                            _authState.value =
                                AuthState.Error("Can't not check info user")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Có lỗi xảy ra")
                }
            }
        println("CHECKLOADING: ${AuthState.Loading}")
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
                    val userDocRef = firestore.collection("users").document(
                        uid
                    )
                    val newUserInfo = UserInfo(uid, "", firstname, lastname, "")
                    userDocRef.set(newUserInfo).addOnSuccessListener {
                        _authState.value = AuthState.Authenticated
                    }.addOnFailureListener { e ->
                        _authState.value = AuthState.Error("Can't not create info user")
                    }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}