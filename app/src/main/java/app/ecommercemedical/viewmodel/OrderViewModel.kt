package app.ecommercemedical.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.data.model.WishList
import app.ecommercemedical.data.model.WishListProduct
import app.ecommercemedical.data.repository.OrderRepository


class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {
    private val _wishList = MutableLiveData<WishList>()
    private var _updateStatus = MutableLiveData<String?>()
    val wishList: LiveData<WishList> = _wishList
    val updateStatus: LiveData<String?> = _updateStatus
    private val _orders = MutableLiveData<List<OrderItem>>()
    val orders: LiveData<List<OrderItem>> = _orders

    fun loadWishList(wishListId: String) {
        orderRepository.getWishlist(
            wishlistId = wishListId,
            onSuccess = { data ->
                _wishList.value = data
            },
            onError = { e ->
                Log.e("OrderViewModel", "Error fetching wishlist: ${e.message}")
            })
    }

    fun addWishListProduct(wishListId: String, newItem: WishListProduct) {
        orderRepository.getWishlist(
            wishlistId = wishListId,
            onSuccess = { data ->
                val currentItems = data.items.toMutableList()
                val existingItem = currentItems.find { it?.productId == newItem.productId }
                if (existingItem != null) {
                    existingItem.quantity += newItem.quantity
                    _updateStatus.value = "duplicate"
                } else {
                    currentItems += newItem
                }
                val updatedWishList = WishList(userId = data.userId, items = currentItems)
                orderRepository.uploadWishList(
                    wishlistId = wishListId,
                    upLoadWishList = updatedWishList,
                    onSuccess = {
                        _wishList.value = updatedWishList
                        _updateStatus.value = "success"
                    },
                    onError = { e ->
                        Log.e("OrderViewModel", "Error updating wishlist: ${e.message}")
                        _updateStatus.value = "error: ${e.message}"
                    }
                )
            },
            onError = { e ->
                Log.e("OrderViewModel", "Error fetching wishlist: ${e.message}")
            })
    }

    fun removeProductFromWishList(wishListId: String, wishListProduct: String?) {
        val currentWishList = _wishList.value
        val updatedItems =
            currentWishList?.items?.filter { it?.productId != wishListProduct }
        println("CHECK DELETED: ($wishListProduct) $updatedItems")
        if (updatedItems != null) {
            val updatedWishList = currentWishList.copy(items = updatedItems)
            orderRepository.uploadWishList(
                wishlistId = wishListId,
                upLoadWishList = updatedWishList,
                onSuccess = {
                    _wishList.value = updatedWishList
                    _updateStatus.value = "deleted"
                },
                onError = { e ->
                    _updateStatus.value = "error: ${e.message}"
                }
            )
        }
    }

    fun updateProductQuantity(wishListId: String, productId: String, newQuantity: Int) {
        orderRepository.getWishlist(
            wishlistId = wishListId,
            onSuccess = { data ->
                val updatedItems = data.items.map { item ->
                    if (item?.productId == productId) {
                        item.copy(quantity = newQuantity)
                    } else {
                        item
                    }
                }
                val updatedWishList = WishList(userId = data.userId, items = updatedItems)
                orderRepository.uploadWishList(
                    wishlistId = wishListId,
                    upLoadWishList = updatedWishList,
                    onSuccess = {
                        _wishList.value = updatedWishList
                    },
                    onError = { e ->
                        Log.e("OrderViewModel", "Error updating quantity: ${e.message}")
                    }
                )
            },
            onError = { e ->
                Log.e("OrderViewModel", "Error fetching wishlist: ${e.message}")
            }
        )
    }

    fun placeOrder(orderItem: OrderItem, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        orderRepository.createOrder(orderItem, { orderId ->
            orderRepository.addOrderToUser(orderItem.userId, orderId, {
                onSuccess()
            }, { e ->
                onError(e)
            })
        }, { e ->
            onError(e)
        })
    }

    fun loadOrders(orderIds: List<String>) {
        orderRepository.getOrdersByIds(
            orderIds = orderIds,
            onSuccess = { orders ->
                _orders.value = orders
            },
            onError = { e ->
                Log.e("OrderViewModel", "Error fetching orders: ${e.message}")
                _orders.value = emptyList()
            }
        )
    }
}
