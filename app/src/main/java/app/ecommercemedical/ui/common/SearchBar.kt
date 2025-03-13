package app.ecommercemedical.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingBar() {
    var querySearch by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .semantics { isTraversalGroup = false }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Hello", modifier = Modifier,
                    fontWeight = FontWeight(500),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "John", modifier = Modifier,
                    fontWeight = FontWeight(700),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            BadgeButton()
        }
        SearchBar(
            modifier = Modifier
                .padding(
                    horizontal = 14.dp
                )
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            shape = MaterialTheme.shapes.medium,
            query = querySearch,
            onQueryChange = { querySearch = it },
            onSearch = { performSearch(it) },
            active = expanded,
            onActiveChange = { expanded = it },
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
            }
        ) {
            //Handle late
        }
    }
}

fun performSearch(query: String) {
    println("Searching for: $query")
}