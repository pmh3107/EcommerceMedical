package app.ecommercemedical.ui.screens.home

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
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.ecommercemedical.ui.common.SearchingBar
import app.ecommercemedical.ui.dataUI.sampleCategories
import app.ecommercemedical.viewmodel.AuthViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest


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

// Data model cho danh mục sản phẩm
data class CategoryItem(
    val id: Int,
    val name: String,
    val iconRes: Int
)


// Composable hiển thị từng mục danh mục
@Composable
fun CategoryItemView(
    category: app.ecommercemedical.ui.dataUI.CategoryItem,
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
                .clip(RoundedCornerShape(40.dp))
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
    categories: List<app.ecommercemedical.ui.dataUI.CategoryItem> = sampleCategories,
    onCategoryClick: (CategoryItem) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryItemView(category = category, onClick = { })
        }
    }
}






