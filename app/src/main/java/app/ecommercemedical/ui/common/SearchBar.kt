import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingBar(
    name: String?,
    products: List<ProductItem>,
    onProductClick: (ProductItem) -> Unit
) {
    var querySearch by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val itemHistory = remember { mutableStateListOf("Fresh Aloe Vera", "Green Tea Bags") }
    var filteredProducts: List<ProductItem> = emptyList()
    val colors = SearchBarDefaults.colors()

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(querySearch) {
        coroutineScope.launch {
            isLoading = true
            delay(1000)
            filteredProducts = products.filter { product ->
                product.name.contains(querySearch, ignoreCase = true)
            }
            isLoading = false
        }
    }


    val inputFieldComposable: @Composable () -> Unit = {
        SearchBarDefaults.InputField(
            query = querySearch,
            onQueryChange = { querySearch = it },
            onSearch = {
                if (querySearch.isNotEmpty() && !itemHistory.contains(querySearch)) {
                    itemHistory.add(0, querySearch)
                }
                expanded = false
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            enabled = true,
            placeholder = { Text("Search product ...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search icon"
                )
            },
            trailingIcon = {
                if (querySearch.isNotEmpty()) {
                    IconButton(onClick = { querySearch = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            interactionSource = remember { MutableInteractionSource() }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = false }
    ) {
        SearchBar(
            inputField = inputFieldComposable,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            shape = MaterialTheme.shapes.medium,
            colors = colors,
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            windowInsets = SearchBarDefaults.windowInsets,
            content = {
                LazyColumn {
                    item {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        } else if (querySearch.isEmpty()) {
                            itemHistory.forEach { historyItem ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .clickable {
                                            querySearch = historyItem
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "History"
                                    )
                                    Text(
                                        text = historyItem,
                                        modifier = Modifier.padding(
                                            vertical = 24.dp,
                                            horizontal = 10.dp
                                        )
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.5.dp)
                                        .background(Color.Gray)
                                )
                            }
                        } else {
                            if (filteredProducts.isNotEmpty()) {
                                filteredProducts.forEach { product ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp)
                                            .clickable {
                                                querySearch = product.name
                                                expanded = false
                                                onProductClick(product)
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Product"
                                        )
                                        Text(
                                            text = product.name,
                                            modifier = Modifier.padding(
                                                vertical = 24.dp,
                                                horizontal = 10.dp
                                            )
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(0.5.dp)
                                            .background(Color.Gray)
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        "Empty result !",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

