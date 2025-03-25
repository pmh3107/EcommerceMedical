package app.ecommercemedical.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userID = MutableLiveData<String>()
    val userID: LiveData<String> = _userID

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = AuthState.Loading
        authRepository.checkAuthStatus(
            onSuccess = { uid, isAuthenticated ->
                _userID.value = uid ?: ""
                _authState.value = if (isAuthenticated) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
            },
            onError = { e ->
                _authState.value = AuthState.Error(e.message ?: "Error checking auth status")
            }
        )
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        authRepository.login(
            email = email,
            password = password,
            onSuccess = { uid ->
                _userID.value = uid
                _authState.value = AuthState.Authenticated
            },
            onError = { e ->
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        )
    }

    fun signup(email: String, password: String, firstname: String, lastname: String) {
        _authState.value = AuthState.Loading
        authRepository.signup(
            email = email,
            password = password,
            firstname = firstname,
            lastname = lastname,
            onSuccess = { uid ->
                _userID.value = uid
                _authState.value = AuthState.Authenticated
            },
            onError = { e ->
                _authState.value = AuthState.Error(e.message ?: "Signup failed")
            }
        )
    }

    fun signout() {
        authRepository.signout(
            onSuccess = {
                _userID.value = ""
                _authState.value = AuthState.Unauthenticated
            },
            onError = { e ->
                _authState.value = AuthState.Error(e.message ?: "Sign out failed")
            }
        )
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}