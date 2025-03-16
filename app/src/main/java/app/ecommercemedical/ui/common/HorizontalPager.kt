package app.ecommercemedical.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import app.ecommercemedical.R
import app.ecommercemedical.ui.screens.loading.LoadingScreen
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun HorizontalPagerCustom(imageUrls: List<String>? = null) {
    var imagesBanner by remember { mutableStateOf<List<String>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val db = Firebase.firestore

    LaunchedEffect(Unit) {
        db.collection("banners").document("banner_list")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val urls = document.get("urls") as? List<String>
                    imagesBanner = urls
                    isLoading = false
                } else {
                    error = "Document not found"
                    isLoading = false
                }
            }
            .addOnFailureListener { e ->
                error = "Lỗi khi lấy dữ liệu: $e"
                isLoading = false
            }
    }

    val defaultImages = listOf(
        "https://picsum.photos/seed/1/600/400",
        "https://picsum.photos/seed/2/600/400",
        "https://picsum.photos/seed/3/600/400",
        "https://picsum.photos/seed/4/600/400",
        "https://picsum.photos/seed/5/600/400"
    )
    val images = imagesBanner ?: imageUrls ?: defaultImages

    if (isLoading) {
        LoadingScreen()
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        }
    } else {
        val state = rememberPagerState { images.size }
        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[page])
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.img_not_found),
                    contentDescription = "Product Detail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        DotsIndicator(
            dotCount = images.size,
            type = ShiftIndicatorType(
                DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 10.dp
                )
            ),
            pagerState = state
        )
    }
}