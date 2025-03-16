package app.ecommercemedical.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.navigation.ProductDetail
import app.ecommercemedical.ui.common.HorizontalPagerCustom
import app.ecommercemedical.ui.dataUI.ProductCard
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


val listProducts: List<ProductCard> = listOf(
    ProductCard(
        "1",
        "Product 1",
        "https://picsum.photos/id/237/200/300",
        "Product description 1"
    ),
    ProductCard(
        "2",
        "Product 2",
        "https://picsum.photos/id/234/200/300",
        "Product description 2"
    ),
    ProductCard(
        "3",
        "Product 3",
        "https://picsum.photos/id/233/200/300",
        "Product description 3"
    ),
    ProductCard(
        "4",
        "Product 4",
        "https://picsum.photos/id/232/200/300",
        "Product description 4"
    ),
    ProductCard(
        "5",
        "Product 5",
        "https://picsum.photos/id/231/200/300",
        "Product description 5"
    ),
    ProductCard(
        "6",
        "Product 6",
        "https://picsum.photos/id/230/200/300",
        "Product description 6"
    )
)

@Composable
fun ViewListCard(modifier: Modifier = Modifier, navController: NavController) {
    val listProduct: List<ProductCard> = listProducts

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HorizontalPagerCustom()
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Category",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            CategorySection()
            Text(
                "List Product",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(listProduct.chunked(2)) { rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                for (item in rowItems) {
                    ProductCard(
                        product = item,
                        modifier = Modifier.weight(1f),
                        navController = navController
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ProductCard(modifier: Modifier = Modifier, product: ProductCard, navController: NavController) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate(ProductDetail.route) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                error = painterResource(R.drawable.img_not_found),
                onError = { error ->
                    android.util.Log.e(
                        "ImageError",
                        "Failed to load image: ${error.result.throwable}"
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.desc,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}