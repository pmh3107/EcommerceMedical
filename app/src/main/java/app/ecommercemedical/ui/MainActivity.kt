package app.ecommercemedical.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class HandleView : ViewModel() {
    private var _isNavigation = MutableStateFlow(false)
    val isNavigation: StateFlow<Boolean> = _isNavigation
    fun handleNavigation() {
        _isNavigation.value = !_isNavigation.value
    }
}

@Composable
fun Greetings(names: List<String> = List(1000) { "Name - $it" }) {
    Surface {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = names) { name -> Greeting(name = name) }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }
    val handleExpandedPadding by animateDpAsState(
        if (expanded) 46.dp else 0.dp, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Surface(
        modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(20),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = handleExpandedPadding.coerceAtLeast(0.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Hello",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp)
                )
                Text(
                    text = "$name!",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 26.sp,
                    )
                )
                if (expanded) {
                    Text(text = ("This is information of $name").repeat(4))
                }
            }
            ElevatedButton(modifier = Modifier.padding(24.dp), onClick = { expanded = !expanded }) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Composable
fun OnBoardShow(onClickNavigate: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome to my App",
                modifier = Modifier.padding(bottom = 24.dp),
                fontSize = 30.sp,
                color = Color.Gray
            )
            FilledTonalButton(onClick = onClickNavigate) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun MyApp(viewModel: HandleView = HandleView()) {
    val isNavigation by viewModel.isNavigation.collectAsState()
    if (isNavigation) {
        Greetings()
    } else {
        OnBoardShow(onClickNavigate = { viewModel.handleNavigation() })
    }
}
