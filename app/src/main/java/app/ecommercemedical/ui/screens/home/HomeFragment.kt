package app.ecommercemedical.ui.screens.home

import MainPushProduct
import SearchingBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.navigation.Checkout
import app.ecommercemedical.ui.common.BadgeButton
import app.ecommercemedical.ui.dataUI.CategoryItem
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun Home(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val imageBanner by productViewModel.imageBanner.observeAsState(emptyList())
    val categoryList by productViewModel.categoryList.observeAsState(emptyList())
    val listProduct by productViewModel.productList.observeAsState(emptyList())
    val uid by authViewModel.userID.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
    val wishListId by userViewModel.wishList.observeAsState()
    println("CHECK WISHLIST: $wishListId")
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        productViewModel.loadBanner()
        productViewModel.loadCategory()
        productViewModel.loadListProduct()
        if (uid.toString().isNotEmpty() && userInfo == null) {
            userViewModel.loadUserInfo(uid.toString())
        }
        // Push product
        // MainPushProduct()
        isLoading = false
    }
    Surface(
        modifier = Modifier
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { traversalIndex = 0f }
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                    BadgeButton(onNavigateCheckout = { navController.navigate(Checkout.route) })
                }
                SearchingBar(
                    name = userInfo?.lastName.toString(),
                    products = listProduct,
                    onProductClick = { product ->
                        navController.navigate("product_detail/${product.id}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ViewListCard(
                    navController = navController,
                    imageBanner = imageBanner,
                    categoryList = categoryList,
                    listProduct = listProduct
                )
            }
        }
    }
}








