import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.round
import kotlin.random.Random

data class Product(
    val id: String,
    val name: String,
    val imageUrl: List<String>,
    val desc: String,
    val price: Double,
    val stockQuantity: Int,
    val categoryId: String
)

fun Double.roundTo(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun generateSampleProducts(): List<Product> {
    val categories = mapOf(
        "category_1" to "Fresh plant",
        "category_2" to "Tea",
        "category_3" to "Medicinal wine",
        "category_4" to "Tea bags",
        "category_5" to "Supplements"
    )
    val products = mutableListOf<Product>()
    var productIdCounter = 1

    val freshPlantNames = listOf(
        "Fresh Aloe Vera for Skin Healing",
        "Fresh Calendula for Wound Care",
        "Fresh Echinacea for Immune Support",
        "Fresh Peppermint for Digestive Health",
        "Fresh Sage for Respiratory Health"
    )
    val teaNames = listOf(
        "Chamomile Tea for Sleep Aid",
        "Peppermint Tea for Digestive Relief",
        "Ginger Tea for Nausea",
        "Turmeric Tea for Anti-Inflammatory",
        "Lavender Tea for Stress Relief"
    )
    val medicinalWineNames = listOf(
        "Elderberry Wine for Cold and Flu",
        "Dandelion Wine for Detoxification",
        "Raspberry Wine for Women's Health",
        "Hibiscus Wine for Blood Pressure",
        "Rosehip Wine for Vitamin C Boost"
    )
    val teaBagsNames = listOf(
        "Green Tea Bags for Antioxidants",
        "Black Tea Bags for Energy Boost",
        "Rooibos Tea Bags for Heart Health",
        "Matcha Tea Bags for Focus",
        "Chamomile Tea Bags for Relaxation"
    )
    val supplementsNames = listOf(
        "Vitamin D Supplement for Bone Health",
        "Probiotic Supplement for Gut Health",
        "Omega-3 Supplement for Heart Health",
        "Magnesium Supplement for Muscle Relaxation",
        "Turmeric Supplement for Joint Health"
    )

    val allNames =
        listOf(freshPlantNames, teaNames, medicinalWineNames, teaBagsNames, supplementsNames)

    // Use categories.entries.withIndex() instead of categories.withIndex()
    for ((index, entry) in categories.entries.withIndex()) {
        val categoryId = entry.key
        val names = allNames[index]
        for (i in 0 until 5) {
            val name = names[i]
            val imageUrl = listOf(
                "https://picsum.photos/seed/product_$productIdCounter/300/200",
                "https://picsum.photos/seed/product_${productIdCounter + 10}/300/200",
                "https://picsum.photos/seed/product_${productIdCounter + 5}/300/200"
            )
            val desc = "This is a description for $name."
            val price = Random.nextDouble(10.0, 100.0).roundTo(2)
            val stockQuantity = Random.nextInt(10, 101)
            val product = Product(
                id = "product_$productIdCounter",
                name = name,
                imageUrl = imageUrl,
                desc = desc,
                price = price,
                stockQuantity = stockQuantity,
                categoryId = categoryId
            )
            products.add(product)
            productIdCounter++
        }
    }
    return products
}

fun saveProductsToFirestore(db: FirebaseFirestore, products: List<Product>) {
    val productsList = products.map { product ->
        hashMapOf(
            "id" to product.id,
            "name" to product.name,
            "imageUrl" to product.imageUrl,
            "desc" to product.desc,
            "price" to product.price,
            "stockQuantity" to product.stockQuantity,
            "categoryId" to product.categoryId
        )
    }

    val productsData = hashMapOf("products" to productsList)

    db.collection("product").document("product_list")
        .set(productsData)
        .addOnSuccessListener {
            println("Danh sách 25 sản phẩm đã được lưu thành công!")
        }
        .addOnFailureListener { e ->
            println("Lỗi khi lưu danh sách: $e")
        }
}

// Example usage
fun MainPushProduct() {
    val db = FirebaseFirestore.getInstance()
    val sampleProducts = generateSampleProducts()
    saveProductsToFirestore(db, sampleProducts)
}