package app.ecommercemedical.ui.screens.product

import ProductItem
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.data.model.WishListProduct
import app.ecommercemedical.navigation.Cart
import app.ecommercemedical.ui.common.BadgeButton
import app.ecommercemedical.ui.common.HorizontalPagerCustom
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailFragment(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    productId: String,
) {
    val productViewModel: ProductViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val product: ProductItem? = productViewModel.getProductById(productId)
    val context = LocalContext.current
    val wishListId by userViewModel.wishList.observeAsState()
    val uid by authViewModel.userID.observeAsState()
    val updateStatus by orderViewModel.updateStatus.observeAsState()

    LaunchedEffect(Unit) {
        if (uid.toString().isNotEmpty()) {
            userViewModel.loadUserInfo(uid.toString())
        }
        productViewModel.loadListProduct()
    }

    LaunchedEffect(updateStatus) {
        updateStatus?.let { status ->
            if (status.startsWith("success")) {
                Toast.makeText(context, "Add ${product?.name} Successfully", Toast.LENGTH_SHORT)
                    .show()
            } else if (status.startsWith("error")) {
                Toast.makeText(context, "Error occurs: $status", Toast.LENGTH_SHORT).show()
            } else if (status.startsWith("duplicate")) {
                Toast.makeText(
                    context,
                    "Add more ${product?.name}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
    val isProductReady = product != null && wishListId.toString().isNotEmpty()
    if (!isProductReady) {
        LoadingScreen()
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
                BadgeButton(onNavigateCheckout = { navController.navigate(Cart.route) })
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
                        onClick = {
                            val newWishListProduct =
                                WishListProduct(productId = product.id, quantity = 1)
                            orderViewModel.addWishListProduct(
                                wishListId = wishListId ?: "",
                                newItem = newWishListProduct,
                            )
                            navController.navigate(Cart.route)
                        },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Buy now")
                    }
                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            val newWishListProduct =
                                WishListProduct(productId = product.id, quantity = 1)
                            orderViewModel.addWishListProduct(
                                wishListId = wishListId ?: "",
                                newItem = newWishListProduct,
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Add to cart")
                    }
                }
            } else {
//                Box(modifier = Modifier.fillMaxSize()) {
//                    Text(
//                        "PRODUCT NOT FOUND !",
//                        modifier = Modifier.fillMaxSize(),
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight(600)
//                    ) // handle late
//                }
            }
        }
    }
}




