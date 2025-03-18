package app.ecommercemedical.ui.screens.product

import ProductItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.navigation.Checkout
import app.ecommercemedical.ui.common.BadgeButton
import app.ecommercemedical.ui.common.HorizontalPagerCustom
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.ProductViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailFragment(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    productId: String
) {
    var isLoading by remember { mutableStateOf(false) }
    val productViewModel: ProductViewModel = viewModel()
    val product: ProductItem? = productViewModel.getProductById(productId)
    LaunchedEffect(Unit) {
        isLoading = true
        productViewModel.loadListProduct()
        isLoading = false
    }
    LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                IconButton(modifier = Modifier, onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    "Product Detail", modifier = Modifier,
                    fontWeight = FontWeight(700),
                    style = MaterialTheme.typography.titleLarge
                )
                BadgeButton(onNavigateCheckout = { navController.navigate(Checkout.route) })
            }
        }
        item {
            if (product != null) {
                HorizontalPagerCustom(imageUrls = product.imageUrl)

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Text(
                    product.name,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(600)
                )
                Spacer(
                    modifier = Modifier.height(14.dp)
                )
                Text(
                    "$ ${product.price}",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight(600)
                )
                Spacer(
                    modifier = Modifier.height(14.dp)
                )
                Text(
                    product.desc + product.desc,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(
                    modifier = Modifier.height(14.dp)
                )
                Text(
                    "Quantity: ${product.stockQuantity}",
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Buy now")
                    }
                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Add to cart")
                    }
                }
            } else if (isLoading) {
                LoadingScreen()
            } else {
//                Box(modifier = Modifier.fillMaxSize()) {
//                    Text(
//                        "PRODUCT NOT FOUND !",
//                        modifier = Modifier.fillMaxSize(),
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight(600)
//                    )
//                }// handle late
                LoadingScreen()
            }
        }
    }
}




