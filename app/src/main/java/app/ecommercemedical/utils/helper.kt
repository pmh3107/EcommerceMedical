package app.ecommercemedical.utils

import ProductItem
import android.util.Patterns
import app.ecommercemedical.data.model.WishListProduct
import kotlin.math.round

fun calculateTotalPrice(wishlist: List<WishListProduct?>, productList: List<ProductItem>): Double {
    return wishlist.sumOf { item ->
        val product = productList.find { it.id == item?.productId }
        item?.quantity?.let { product?.price?.times(it) } ?: 0.0
    }
}

fun Double.roundTo(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun isNameValid(name: String): Boolean {
    return name.isNotBlank() && name.length >= 2
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }
}

fun getPasswordErrorMessage(password: String): String {
    return when {
        password.length < 8 -> "Password must be at least 8 characters long."
        !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter."
        !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter."
        !password.any { it.isDigit() } -> "Password must contain at least one digit."
        else -> ""
    }
}

fun isFormValid(
    isErrorFirstname: Boolean,
    isErrorLastname: Boolean,
    isErrorEmail: Boolean,
    isErrorPassword: Boolean,
    isErrorConfirmPassword: Boolean
): Boolean {
    return !isErrorFirstname && !isErrorLastname && !isErrorEmail && !isErrorPassword && !isErrorConfirmPassword
}