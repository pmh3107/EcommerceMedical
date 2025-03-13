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
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.navigation.ProductDetail
import app.ecommercemedical.ui.common.HorizontalPagerCustom
import app.ecommercemedical.ui.dataUI.ProductCard
import app.ecommercemedical.ui.dataUI.listProduct
import coil.compose.AsyncImage

val imagesBanner = listOf(
    "https://picsum.photos/seed/1/300/400",
    "https://picsum.photos/seed/2/300/400",
    "https://picsum.photos/seed/3/300/400",
    "https://picsum.photos/seed/4/300/400",
    "https://picsum.photos/seed/5/300/400"
)

@Composable
fun ViewListCard(modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HorizontalPagerCustom(imagesBanner)
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