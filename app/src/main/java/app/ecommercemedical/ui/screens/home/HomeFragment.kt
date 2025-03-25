package app.ecommercemedical.ui.screens.home

import SearchingBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.navigation.Cart
import app.ecommercemedical.navigation.Profile
import app.ecommercemedical.ui.common.BadgeButton
import app.ecommercemedical.ui.common.ViewListCard
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.utils.showSimpleNotification
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel

@Composable
fun Home(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val imageBanner by productViewModel.imageBanner.observeAsState()
    val categoryList by productViewModel.categoryList.observeAsState()
    val listProduct by productViewModel.productList.observeAsState()
    val uid by authViewModel.userID.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
    var isLoading by remember { mutableStateOf(false) }
    val isLoadingProduct by productViewModel.isLoading.observeAsState(false)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        isLoading = true
        productViewModel.loadBanner()
        productViewModel.loadCategory()
        productViewModel.loadListProduct()
        if (uid.toString().isNotEmpty() && userInfo == null) {
            userViewModel.loadUserInfo(uid.toString())
        }
        isLoading = false
    }
    val isReady =
        listProduct != null && categoryList != null && imageBanner != null && userInfo != null && uid.toString()
            .isNotEmpty()

    if (!isReady) {
        LoadingScreen()
    } else {
        Surface(
            modifier = Modifier
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { traversalIndex = 0f }
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        modifier = Modifier,
                        shape = MaterialTheme.shapes.medium,
                        onClick = { navController.navigate(Profile.route) }) {
                        Column {
                            Text(
                                "Hello",
                                fontWeight = FontWeight(500),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                userInfo?.lastName.toString(),
                                fontWeight = FontWeight(700),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        BadgeButton(
                            onNavigateCheckout = { navController.navigate(Cart.route) })
                        IconButton(onClick = {
                            println("IT HAVE ACTIVE")
                            showSimpleNotification(context, "TEST")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null
                            )
                        }
                    }
                }
                SearchingBar(
                    name = userInfo?.lastName.toString(),
                    products = listProduct ?: emptyList(),
                    onProductClick = { product ->
                        navController.navigate("product_detail/${product.id}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ViewListCard(
                    navController = navController,
                    imageBanner = imageBanner ?: emptyList(),
                    categoryList = categoryList ?: emptyList(),
                    listProduct = listProduct ?: emptyList(),
                    isLoading = isLoadingProduct,
                    loadMoreProducts = { productViewModel.loadMoreProducts() }
                )
            }
        }
    }
}









