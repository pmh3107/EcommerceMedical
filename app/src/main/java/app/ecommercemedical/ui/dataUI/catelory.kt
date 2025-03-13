package app.ecommercemedical.ui.dataUI

data class CategoryItem(
    val id: Int,
    val name: String,
    val imageUrl: String
)

val sampleCategories = listOf(
    CategoryItem(1, "Electronics", "https://loremflickr.com/320/240/electronics"),
    CategoryItem(2, "Fashion", "https://loremflickr.com/320/240/fashion"),
    CategoryItem(3, "Home", "https://loremflickr.com/320/240/home"),
    CategoryItem(4, "Beauty", "https://loremflickr.com/320/240/beauty"),
    CategoryItem(5, "Sports", "https://loremflickr.com/320/240/sports")
)
