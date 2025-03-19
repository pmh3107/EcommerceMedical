package app.ecommercemedical.ui.screens.home

import CategoryItem
import CategorySection
import ProductItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.ui.common.HorizontalPagerCustom
import coil.compose.AsyncImage

@Composable
fun ViewListCard(
    modifier: Modifier = Modifier,
    navController: NavController,
    imageBanner: List<String?>,
    categoryList: List<CategoryItem>,
    listProduct: List<ProductItem>
) {
    val listProduct: List<ProductItem> = listProduct

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            HorizontalPagerCustom(imageUrls = imageBanner)
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
            CategorySection(categoryList)
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
fun ProductCard(
    modifier: Modifier = Modifier, product: ProductItem, navController: NavController
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate("product_detail/${product.id}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.imageUrl[1],
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                error = painterResource(R.drawable.img_not_found),
                placeholder = painterResource(R.drawable.img_not_found),
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$ ${product.price}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight(700)
            )
        }
    }
}