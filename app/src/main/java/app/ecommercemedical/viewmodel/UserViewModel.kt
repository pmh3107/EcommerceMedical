package app.ecommercemedical.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.model.UserInfo
import app.ecommercemedical.data.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo?>()
    private var _updateStatus = MutableLiveData<String?>()
    val userInfo: LiveData<UserInfo?> = _userInfo
    val updateStatus: LiveData<String?> = _updateStatus

    fun loadUserInfo(uid: String) {
        userRepository.fetchUserInfo(
            uid = uid,
            onSuccess = { info ->
                _userInfo.value = info
            },
            onError = { e ->
                Log.e("UserViewModel", "Error fetching user: ${e.message}")
                _userInfo.value = null
            }
        )
    }

    fun updateUserInfo(uid: String, updatedProfile: UserInfo) {
        userRepository.updateUserInfo(
            uid = uid,
            updatedProfile = updatedProfile,
            onSuccess = {
                _userInfo.value = updatedProfile
                _updateStatus.value = "success"
            },
            onError = { e ->
                Log.e("UserViewModel", "Error updating user: ${e.message}")
                _updateStatus.value = "error occurs: ${e.message} "
            }
        )
    }
}
