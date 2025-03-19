package app.ecommercemedical.viewmodel

import CategoryItem
import ProductItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.ecommercemedical.data.repository.ProductRepository

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    private val _imageBanner = MutableLiveData<List<String>>()
    private var _categoryList = MutableLiveData<List<CategoryItem>>()
    private val _productList = MutableLiveData<List<ProductItem>>()
    val imageBanner: LiveData<List<String>> = _imageBanner
    val categoryList: LiveData<List<CategoryItem>> = _categoryList
    val productList: LiveData<List<ProductItem>> = _productList


    fun loadBanner() {
        productRepository.getBannerData(
            onSuccess = { urls ->
                _imageBanner.value = urls
            },
            onError = { e ->
                Log.e("ProductViewModel", "Error fetching banner data: ${e.message}")
                _imageBanner.value = emptyList()
            }
        )
    }

    fun loadCategory() {
        productRepository.getCategory(
            onSuccess = { category -> _categoryList.value = category },
            onError = { e ->
                Log.e("ProductViewModel", "Error fetching category data: ${e.message}")
                _categoryList.value = emptyList()
            }
        )
    }

    fun loadListProduct() {
        productRepository.getListProduct(
            onSuccess = { products ->
                _productList.value = products
            },
            onError = { e ->
                Log.e("ProductViewModel", "Error fetching product list: ${e.message}")
                _productList.value = emptyList()
            }
        )
    }

    fun getProductById(productId: String): ProductItem? {
        return _productList.value?.find { it.id == productId }
    }
}
