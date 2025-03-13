package app.ecommercemedical.ui.dataUI

class mainActivity {
}

data class ProductCard(val id: String, val name: String, val imageUrl: String, val desc: String)

val listProduct = listOf(
    ProductCard(
        id = "1",
        name = "Product 1",
        imageUrl = "https://picsum.photos/id/237/200/300",
        desc = "Product description 1"
    ),
    ProductCard(
        id = "2",
        name = "Product 2",
        imageUrl = "https://picsum.photos/id/234/200/300",
        desc = "Product description 2"
    ),
    ProductCard(
        id = "3",
        name = "Product 3",
        imageUrl = "https://picsum.photos/id/233/200/300",
        desc = "Product description 3"
    ),
    ProductCard(
        id = "3",
        name = "Product 3",
        imageUrl = "https://picsum.photos/id/232/200/300",
        desc = "Product description 3"
    ),
    ProductCard(
        id = "3",
        name = "Product 3",
        imageUrl = "https://picsum.photos/id/231/200/300",
        desc = "Product description 3"
    ),
    ProductCard(
        id = "3",
        name = "Product 3",
        imageUrl = "https://picsum.photos/id/230/200/300",
        desc = "Product description 3"
    ),
)