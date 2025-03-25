package app.ecommercemedical.ui.screens.cart

import AddressScreen
import ProductItem
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.material3.TextField
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
import app.ecommercemedical.data.model.WishListProduct
import app.ecommercemedical.navigation.Orders
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.utils.calculateTotalPrice
import app.ecommercemedical.utils.roundTo
import app.ecommercemedical.utils.showSimpleNotification
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
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
    val address by remember(userInfo) { mutableStateOf(userInfo?.address.orEmpty()) }
    var isUpdateAddress by remember { mutableStateOf(false) }

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
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .border(
                                    width = 1.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    color = Color.Gray
                                )
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Delivery Address:",
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight(600)
                            )
                            Text(
                                text = address,
                                modifier = Modifier,
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = { isUpdateAddress = !isUpdateAddress },
                            modifier = Modifier
                                .weight(1f),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = "Create",
                            )
                        }
                    }
                    if (isUpdateAddress) {
                        AddressScreen(uid = uid.toString(), { isUpdateAddress = !isUpdateAddress })
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(Color.Gray)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total: ${
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
                                if (address.isNotEmpty()) {
                                    orderViewModel.placeOrder(orderItem, {
                                        orderViewModel.emptyWishList(
                                            wishListId.toString(),
                                            uid.toString()
                                        )
                                        showSimpleNotification(context, "Order placed successfully")
                                        navController.navigate(Orders.route)
                                    }, { e ->
                                        showSimpleNotification(
                                            context,
                                            "Error placing order: ${e.message}"
                                        )
                                    })
                                } else {
                                    showSimpleNotification(
                                        context,
                                        "You need to add address first !"
                                    )
                                }
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
            filteredWishlistItems?.let {
                items(it) { (product, item) ->
                    if (item != null) {
                        CartItemView(
                            product = product,
                            item = item,
                            removeProductFromWishList = {
                                orderViewModel.removeProductFromWishList(
                                    wishListId = wishListId.toString(),
                                    item.productId,
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
    updateQuantity: (Int) -> Unit,
    navigateToProduct: () -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(item.quantity) }

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


