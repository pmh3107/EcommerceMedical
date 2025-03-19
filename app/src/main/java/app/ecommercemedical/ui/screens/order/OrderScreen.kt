package app.ecommercemedical.ui.screens.order


import ProductItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.data.model.OrderItem
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.OrderViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

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
    val orders by orderViewModel.orders.observeAsState()
    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.productList.observeAsState(initial = emptyList())
    LaunchedEffect(uid) {
        if (uid != null && uid!!.isNotEmpty()) {
            userViewModel.loadUserInfo(uid!!)
        }
    }

    LaunchedEffect(userInfo) {
        if (userInfo != null && userInfo!!.orders.isNotEmpty()) {
            orderViewModel.loadOrders(userInfo!!.orders)
        }
        productViewModel.loadListProduct()
    }

    println("CHECK ORDER: $orders")
    val productMap = remember(products) {
        products.associateBy { it.id }
    }
    if (userInfo == null || orders == null || products.isEmpty()) {
        return LoadingScreen()
    }

    val pendingOrders = orders!!.filter { it.status == "pending" }
    val shippingOrders = orders!!.filter { it.status == "shipping" }
    val completedOrders = orders!!.filter { it.status == "completed" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                pendingOrders = pendingOrders,
                shippingOrders = shippingOrders,
                completedOrders = completedOrders,
                productMap = productMap
            )
        }
    }
}

@Composable
fun OrderTabs(
    pendingOrders: List<OrderItem>,
    shippingOrders: List<OrderItem>,
    completedOrders: List<OrderItem>,
    productMap: Map<String, ProductItem>
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Pending", "Shipping", "Completed")

    TabRow(selectedTabIndex = selectedTab) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(title) }
            )
        }
    }

    when (selectedTab) {
        0 -> OrderList(orders = pendingOrders, status = "Pending", productMap = productMap)
        1 -> OrderList(orders = shippingOrders, status = "Shipping", productMap = productMap)
        2 -> OrderList(orders = completedOrders, status = "Completed", productMap = productMap)
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
            Text("Order ID: ${order.orderId}", fontWeight = FontWeight.Bold)
            Text("Date: ${order.orderDate.toDate()}")
            Text("Status: ${order.status}")
            Text("Shipping Address: ${order.shippingAddress}")
            Text("Products:", fontWeight = FontWeight.Bold)
            order.products.forEach { orderProduct ->
                val product = productMap[orderProduct.productId]
                if (product != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageUrl.firstOrNull())
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(" - ${product.name}: ${orderProduct.quantity} items, Price: $${product.price}, Description: ${product.desc}")
                } else {
                    Text(" - Product not found: ${orderProduct.productId}")
                }
            }
        }
    }
}