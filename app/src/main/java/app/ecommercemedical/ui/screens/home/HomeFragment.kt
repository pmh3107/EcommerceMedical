package app.ecommercemedical.ui.screens.home

import SearchingBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.R
import app.ecommercemedical.ui.dataUI.CategoryItem
import app.ecommercemedical.viewmodel.AuthViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

val sampleCategories: List<CategoryItem> = listOf(
    CategoryItem(1, "Fresh plant", "https://loremflickr.com/320/240/electronics"),
    CategoryItem(2, "Tea", "https://loremflickr.com/320/240/fashion"),
    CategoryItem(3, "Medicinal wine", "https://loremflickr.com/320/240/home"),
    CategoryItem(4, "Tea bags", "https://loremflickr.com/320/240/beauty"),
)

@Composable
fun Home(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Surface(
        modifier = Modifier
    ) {
        Column {
            SearchingBar()
            Spacer(modifier = Modifier.height(16.dp))
            ViewListCard(navController = navController)
        }
    }
}


@Composable
fun CategoryItemView(
    category: CategoryItem,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp)),
            placeholder = painterResource(R.drawable.img_not_found)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun CategorySection(
    categories: List<CategoryItem> = sampleCategories,
    onCategoryClick: (CategoryItem) -> Unit = {}
) {
//    var categories by remember { mutableStateOf<List<CategoryItem>?>(null) }
//    val db = Firebase.firestore
//
//    db.collection("categories").document("category_list")
//        .get()
//        .addOnSuccessListener { document ->
//            if (document != null) {
//                val categoriesData = document.get("categories") as? List<CategoryItem>
//                categories = categoriesData
//                println("Danh sách Category: $categories")
//            } else {
//                println("Không tìm thấy document")
//            }
//        }
//        .addOnFailureListener { e ->
//            println("Lỗi khi lấy dữ liệu: $e")
//        }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories ?: emptyList()) { category ->
            CategoryItemView(category = category, onClick = { })
        }
    }
}






