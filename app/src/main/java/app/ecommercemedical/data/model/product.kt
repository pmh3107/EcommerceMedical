data class ProductItem(
    val id: String,
    val name: String,
    val imageUrl: List<String>,
    val desc: String,
    val price: Double,
    val stockQuantity: Int,
    val categoryId: String
)