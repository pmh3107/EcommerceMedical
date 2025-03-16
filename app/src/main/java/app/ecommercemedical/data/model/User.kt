package app.ecommercemedical.data.model

import androidx.lifecycle.ViewModel


//class UserManager : ViewModel() {
//    private var _user: UserInfo? = null
//    val uid = _user?.id
//    fun setUser(userInfo: UserInfo) {
//        this._user = userInfo
//    }
//
//    fun setUid(newUid: String?) {
//        if (newUid != null) {
//            this._user?.id = newUid
//        }
//    }
//}

data class UserInfo(
    var id: String,
    var imageUrl: String,
    var firstName: String,
    var lastName: String,
    var address: String
)