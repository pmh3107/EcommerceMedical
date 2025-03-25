package app.ecommercemedical.ui.screens.order


import ProductItem
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.constants.Status
import app.ecommercemedical.constants.constants
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.navigation.Home
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val uid by authViewModel.userID.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
    val orders by orderViewModel.orders.observeAsState(emptyList())
    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.productList.observeAsState(emptyList())
    val status = Status()
    
    LaunchedEffect(uid) {
        if (uid != null) {
            userViewModel.loadUserInfo(uid.toString())
        }
    }

    LaunchedEffect(userInfo) {
        if (userInfo != null && userInfo!!.orders.isNotEmpty()) {
            orderViewModel.loadOrders(userInfo!!.orders)
            productViewModel.loadListProduct()
        }

    }

    val productMap = remember(products) {
        products.associateBy { it.id }
    }

    if (userInfo == null && products.isEmpty()) {
        LoadingScreen()
    }

    val cancelOrders = orders.filter { it.status == status.canceled }
    val pendingOrders = orders.filter { it.status == status.pending }
    val shippingOrders = orders.filter { it.status == status.shipping }
    val completedOrders = orders.filter { it.status == status.completed }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Home.route) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OrderTabs(
                cancelOrders = cancelOrders,
                pendingOrders = pendingOrders,
                shippingOrders = shippingOrders,
                completedOrders = completedOrders,
                productMap = productMap
            )
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun OrderTabs(
    cancelOrders: List<OrderItem>,
    pendingOrders: List<OrderItem>,
    shippingOrders: List<OrderItem>,
    completedOrders: List<OrderItem>,
    productMap: Map<String, ProductItem>
) {
    var selectedTab by remember { mutableIntStateOf(1) }
    val tabTitles = listOf("Canceled", "Pending", "Shipping", "Completed")
    TabRow(selectedTabIndex = selectedTab) {
        tabTitles.forEachIndexed { index, title ->
            var itemCount by remember { mutableIntStateOf(0) }
            when (title) {
                "Canceled" -> itemCount = cancelOrders.size
                "Pending" -> itemCount = pendingOrders.size
                "Shipping" -> itemCount = shippingOrders.size
                "Completed" -> itemCount = completedOrders.size
            }
            BadgedBox(modifier = Modifier,
                badge = {
                    if (itemCount > 0) {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {
                            Text("$itemCount")
                        }
                    }
                }) {
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
    }

    when (selectedTab) {
        0 -> OrderList(orders = completedOrders, status = "Canceled", productMap = productMap)
        1 -> OrderList(orders = pendingOrders, status = "Pending", productMap = productMap)
        2 -> OrderList(orders = shippingOrders, status = "Shipping", productMap = productMap)
        3 -> OrderList(orders = completedOrders, status = "Completed", productMap = productMap)
    }
}

@Composable
fun OrderList(orders: List<OrderItem>, status: String, productMap: Map<String, ProductItem>) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No $status orders", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
        ) {
            items(orders) { order ->
                OrderItemView(order = order, productMap = productMap)
            }
        }
    }
}

@Composable
fun OrderItemView(order: OrderItem, productMap: Map<String, ProductItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Order ID: ${order.orderId}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Date: ${convertTimestampToDate(order.orderDate)}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Status: ${order.status}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Shipping Address: ${order.shippingAddress}")
            Spacer(modifier = Modifier.height(14.dp))
            Text("Products:", fontWeight = FontWeight.Bold)
            order.products.forEach { orderProduct ->
                val product = productMap[orderProduct.productId]
                if (product != null) {
                    CartItemView(product, orderProduct.quantity)
                } else {
                    Text(" - Product not found: ${orderProduct.productId}")
                }
            }
        }
    }
}

@Composable
fun CartItemView(
    product: ProductItem,
    quantity: Int,
) {
    Card(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
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
            Text(
                "$quantity items",
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 10.dp)
            )
        }
    }
}

fun convertTimestampToDate(timestamp: Timestamp): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
