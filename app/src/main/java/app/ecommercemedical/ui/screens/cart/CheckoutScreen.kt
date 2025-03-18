package app.ecommercemedical.ui.screens.cart

import ProductItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.ui.screens.chat.ChatMessageItem
import app.ecommercemedical.viewmodel.AuthViewModel


data class WishlistItem(
    val productID: String,
    var quantity: Int
)

val products = listOf(
    ProductItem(
        id = "product_1",
        name = "Fresh Aloe Vera for Skin Healing",
        categoryId = "category_1",
        price = 11.37,
        imageUrl = listOf(
            "https://picsum.photos/seed/product_1/300/200",
            "https://picsum.photos/seed/product_11/300/200",
            "https://picsum.photos/seed/product_6/300/200"
        ),
        stockQuantity = 11,
        desc = "This is a description for Fresh Aloe Vera for Skin Healing."
    ),
    ProductItem(
        id = "product_2",
        name = "Organic Green Tea",
        categoryId = "category_2",
        price = 15.99,
        imageUrl = listOf(
            "https://picsum.photos/seed/product_2/300/200",
            "https://picsum.photos/seed/product_22/300/200",
            "https://picsum.photos/seed/product_12/300/200"
        ),
        stockQuantity = 20,
        desc = "Organic Green Tea for a refreshing and healthy experience."
    )
)

// Dummy wishlist data
val wishlist = mutableListOf(
    WishlistItem(productID = "product_1", quantity = 2),
    WishlistItem(productID = "product_2", quantity = 1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        "List Cart",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Price: ${calculateTotalPrice(wishlist)}", fontWeight = FontWeight.Bold)
                Button(onClick = { /* Proceed to checkout */ }) {
                    Text("Check out", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues)
        ) {
            items(wishlist) { item ->
                val product = products.find { it.id == item.productID }
                product?.let {
                    CartItemView(product = it, item = item)
                }
            }
        }
    }
}

@Composable
fun CartItemView(product: ProductItem, item: WishlistItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray, shape = CircleShape)
        ) {
            Text("Image", modifier = Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(product.name, fontWeight = FontWeight.Bold)
            Text("${product.price} USD", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Quantity Control
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (item.quantity > 1) item.quantity-- }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
            Text("${item.quantity}", modifier = Modifier.padding(8.dp))
            IconButton(onClick = { if (item.quantity < product.stockQuantity) item.quantity++ }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
            }
        }
    }
}

fun calculateTotalPrice(wishlist: List<WishlistItem>): Double {
    return wishlist.sumOf { item ->
        val product = products.find { it.id == item.productID }
        product?.price?.times(item.quantity) ?: 0.0
    }
}
