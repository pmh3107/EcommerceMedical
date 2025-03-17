package app.ecommercemedical.ui.screens.home

import MainPushProduct
import SearchingBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.ecommercemedical.ui.dataUI.CategoryItem
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import app.ecommercemedical.viewmodel.AuthViewModel
import app.ecommercemedical.viewmodel.ProductViewModel
import app.ecommercemedical.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

val sampleCategories: List<CategoryItem> = listOf(
    CategoryItem("alo", "Fresh plant", "https://loremflickr.com/320/240/electronics"),
    CategoryItem("lololo", "Tea", "https://loremflickr.com/320/240/fashion"),
    CategoryItem("alalal", "Medicinal wine", "https://loremflickr.com/320/240/home"),
    CategoryItem("áddsa", "Tea bags", "https://loremflickr.com/320/240/beauty"),
)

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val desc: String,
    val price: Double,
    val stockQuantity: Int,
    val categoryId: String
)

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
    println("CHECK CATEGORY: $listProduct")
    val uid by authViewModel.userID.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
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
//        MainPushProduct()
        isLoading = false
    }
    Surface(
        modifier = Modifier
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column {
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








