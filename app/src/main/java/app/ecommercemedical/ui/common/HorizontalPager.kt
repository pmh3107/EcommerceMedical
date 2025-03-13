package app.ecommercemedical.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.ecommercemedical.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType


@Composable
fun HorizontalPagerCustom(imageUrls: List<String>? = null) {
    val images = imageUrls ?: listOf(
        "https://picsum.photos/seed/1/600/400",
        "https://picsum.photos/seed/2/600/400",
        "https://picsum.photos/seed/3/600/400",
        "https://picsum.photos/seed/4/600/400",
        "https://picsum.photos/seed/5/600/400"
    )
    val state = rememberPagerState { images.size }
    androidx.compose.foundation.pager.HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[page])
                    .crossfade(true)
                    .build(),
//                placeholder = painterResource(R.drawable.loading_animation),
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