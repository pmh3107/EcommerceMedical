package app.ecommercemedical.ui.screens.cart

import ProductItem
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.data.model.OrderProduct
import app.ecommercemedical.data.model.WishList
import app.ecommercemedical.data.model.WishListProduct
import app.ecommercemedical.navigation.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val uid by authViewModel.userID.observeAsState()
    val wishlist by orderViewModel.wishList.observeAsState()
    val productList by productViewModel.productList.observeAsState()
    val wishListId by userViewModel.wishList.observeAsState()
    val updateStatus by orderViewModel.updateStatus.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
    val context = LocalContext.current
    var address by remember(userInfo) { mutableStateOf(userInfo?.address.orEmpty()) }
    LaunchedEffect(wishListId) {
        if (uid.toString().isNotEmpty()) {
            userViewModel.loadUserInfo(uid.toString())
        }
        orderViewModel.loadWishList(wishListId.toString())
        productViewModel.loadListProduct()
    }

    LaunchedEffect(updateStatus) {
        updateStatus?.let { status ->
            if (status.startsWith("deleted")) {
                Toast.makeText(context, "Deleted product Successfully !!", Toast.LENGTH_SHORT)
                    .show()
            } else if (status.startsWith("error")) {
                Toast.makeText(context, "Error occurs: $status", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val isDataReady = wishlist != null && productList != null && wishListId.toString().isNotEmpty()

    if (!isDataReady) {
        return LoadingScreen()
    }

    val filteredWishlistItems = wishlist?.items?.mapNotNull { wishlistItem ->
        productList?.find { it.id == wishlistItem?.productId }?.let { product ->
            Pair(product, wishlistItem)
        }
    }

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
                    )
                },
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            if (wishlist?.items?.isEmpty() == false) {
                Column(modifier = Modifier.background(Color.White)) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        label = { Text("Delivery Address") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { })
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:  ${
                                wishlist?.items?.let {
                                    calculateTotalPrice(
                                        it,
                                        productList ?: emptyList()
                                    ).roundTo(2)
                                }
                            } USD",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight(600)
                        )
                        Button(
                            onClick = {
                                val currentItems = wishlist?.items ?: emptyList()
                                val orderProducts = currentItems.mapNotNull { item ->
                                    item?.let { OrderProduct(it.productId, it.quantity) }
                                }
                                val orderItem = OrderItem(
                                    userId = uid.toString(),
                                    orderDate = Timestamp.now(),
                                    products = orderProducts,
                                    shippingAddress = address
                                )
                                orderViewModel.placeOrder(orderItem, {
                                    Toast.makeText(
                                        context,
                                        "Order placed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }, { e ->
                                    Toast.makeText(
                                        context,
                                        "Error placing order: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                            },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Check out", fontWeight = FontWeight.Bold, modifier = Modifier)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (wishlist?.items?.isEmpty() == true && wishListId.toString().isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Empty List !!!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight(600)
                        )
                        TextButton(onClick = { navController.navigate(Home.route) }) {
                            Text(
                                "View product", style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
            filteredWishlistItems?.let {
                items(it) { (product, item) ->
                    if (item != null) {
                        CartItemView(
                            product = product,
                            item = item,
                            removeProductFromWishList = {
                                orderViewModel.removeProductFromWishList(
                                    wishListId = wishListId.toString(),
                                    item.productId as? String
                                )
                            },
                            updateQuantity = { newQuantity ->
                                orderViewModel.updateProductQuantity(
                                    wishListId = wishListId.toString(),
                                    productId = item.productId,
                                    newQuantity = newQuantity
                                )
                            },
                            navigateToProduct = { navController.navigate("product_detail/${item.productId}") }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CartItemView(
    product: ProductItem,
    item: WishListProduct,
    removeProductFromWishList: () -> Unit,
    updateQuantity: (Int) -> Unit, // Thêm tham số này
    navigateToProduct: () -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(item.quantity) }

    // Cập nhật số lượng trong Firestore khi quantity thay đổi
    LaunchedEffect(quantity) {
        updateQuantity(quantity)
    }

    Card(
        onClick = { navigateToProduct() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .background(Color.Gray, shape = CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl[0])
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    placeholder = painterResource(R.drawable.img_not_found),
                    error = painterResource(R.drawable.img_not_found)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    product.name, fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text("${product.price} USD", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                    Text("$quantity", modifier = Modifier.padding(8.dp))
                    IconButton(onClick = {
                        if (quantity < product.stockQuantity) quantity++ else Toast.makeText(
                            context, "Out of stock", Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                    }
                }

                Button(
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                    onClick = { removeProductFromWishList() }) {
                    Text("Delete", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

fun calculateTotalPrice(wishlist: List<WishListProduct?>, productList: List<ProductItem>): Double {
    return wishlist.sumOf { item ->
        val product = productList.find { it.id == item?.productId }
        item?.quantity?.let { product?.price?.times(it) } ?: 0.0
    }
}

fun Double.roundTo(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

