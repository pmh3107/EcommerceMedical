package app.ecommercemedical.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.ecommercemedical.R
import coil.compose.AsyncImage

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

@Composable
fun ViewListCard(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Banner()
            Spacer(modifier = Modifier.height(16.dp))
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
                        modifier = Modifier.weight(1f)
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
fun Banner() {
    val image = painterResource(R.drawable.banner)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
fun ProductCard(modifier: Modifier = Modifier, product: ProductCard) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                error = painterResource(R.drawable.example_img),
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