package app.ecommercemedical.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.ecommercemedical.ui.common.SearchingBar


@Composable
fun Home() {
    Surface(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 80.dp)
    ) {
        Column {
            SearchingBar()
            Spacer(modifier = Modifier.height(16.dp))
            ViewListCard()
        }
    }
}






