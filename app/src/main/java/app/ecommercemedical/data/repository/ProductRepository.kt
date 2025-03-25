package app.ecommercemedical.data.repository

import CategoryItem
import ProductItem
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getBannerData(onSuccess: (List<String>) -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("banners").document("banner_list").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val urls = document.get("urls")
                    if (urls is List<*>) {
                        val stringUrls = urls.filterIsInstance<String>()
                        onSuccess(stringUrls)
                    } else {
                        Log.d("ProductRepository", "Field 'urls' not List type")
                        onSuccess(emptyList())
                    }
                } else {
                    Log.d("ProductRepository", "Document banner_list not exist")
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getCategory(
        onSuccess: (List<CategoryItem>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        firestore.collection("categories").document("category_list")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val categoriesData = document.get("categories")
                    if (categoriesData is List<*>) {
                        val categories = categoriesData.mapNotNull { item ->
                            if (item is Map<*, *>) {
                                val id = item["id"] as? String ?: ""
                                val name = item["name"] as? String ?: ""
                                val imageUrl = item["imageUrl"] as? String ?: ""
                                CategoryItem(id, name, imageUrl)
                            } else {
                                null
                            }
                        }
                        onSuccess(categories)
                    } else {
                        Log.d("ProductRepository", "Field 'categories' is not a list")
                        onSuccess(emptyList())
                    }
                } else {
                    Log.d("ProductRepository", "Document category_list does not exist")
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getListProduct(
        onSuccess: (List<ProductItem>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("product").document("product_list")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val productsData = document.get("products")
                    if (productsData is List<*>) {
                        val products = productsData.mapNotNull { item ->
                            if (item is Map<*, *>) {
                                val id = item["id"] as? String ?: ""
                                val name = item["name"] as? String ?: ""
                                val imageUrl = when (val urlData = item["imageUrl"]) {
                                    is List<*> -> urlData.filterIsInstance<String>()
                                    else -> emptyList()
                                }
                                val desc = item["desc"] as? String ?: ""
                                val price = (item["price"] as? Number)?.toDouble() ?: 0.0
                                val stockQuantity = (item["stockQuantity"] as? Number)?.toInt() ?: 0
                                val categoryId = item["categoryId"] as? String ?: ""
                                ProductItem(
                                    id,
                                    name,
                                    imageUrl,
                                    desc,
                                    price,
                                    stockQuantity,
                                    categoryId
                                )
                            } else {
                                null
                            }
                        }
                        onSuccess(products)
                    } else {
                        onSuccess(emptyList())
                    }
                } else {
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getListProductPaginated(
        page: Int,
        pageSize: Int,
        onSuccess: (List<ProductItem>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val startIndex = (page - 1) * pageSize
        firestore.collection("product")
            .document("product_list")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val productsData = document.get("products")
                    if (productsData is List<*>) {
                        val products =
                            productsData.drop(startIndex).take(pageSize).mapNotNull { item ->
                                if (item is Map<*, *>) {
                                    val id = item["id"] as? String ?: ""
                                    val name = item["name"] as? String ?: ""
                                    val imageUrl = item["imageUrl"] as? List<String> ?: emptyList()
                                    val desc = item["desc"] as? String ?: ""
                                    val price = (item["price"] as? Number)?.toDouble() ?: 0.0
                                    val stockQuantity =
                                        (item["stockQuantity"] as? Number)?.toInt() ?: 0
                                    val categoryId = item["categoryId"] as? String ?: ""
                                    ProductItem(
                                        id,
                                        name,
                                        imageUrl,
                                        desc,
                                        price,
                                        stockQuantity,
                                        categoryId
                                    )
                                } else null
                            }
                        onSuccess(products)
                    }
                } else {
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener(onError)
    }
}
