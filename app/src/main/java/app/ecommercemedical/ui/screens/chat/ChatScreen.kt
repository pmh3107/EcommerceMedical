package app.ecommercemedical.ui.screens.chat


import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.ecommercemedical.viewmodel.AuthViewModel
import java.util.Calendar
import java.util.Date

data class ChatMessage(
    val id: Int,
    val text: String,
    val isSentByMe: Boolean,
    val timestamp: String
)

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val sdf = SimpleDateFormat("hh:mm")
    val currentTime = sdf.format(Date())
//    val currentTime = Calendar.
    println(" CHECK DATE :  " + currentTime)
    val messages = remember {
        mutableStateListOf(
            ChatMessage(1, "Hello! This is customer service online", false, currentTime),
            ChatMessage(2, "How can we assist you ?", false, currentTime),
        )
    }

    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("My Store", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .height(7.dp)
                                .width(7.dp)
                                .background(color = Color.Green, shape = CircleShape)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textStyle = TextStyle(fontSize = 16.sp),
                    decorationBox = { innerTextField ->
                        if (messageText.isEmpty()) {
                            Text("Input your message ...", color = Color.Gray, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id = messages.size + 1,
                                    text = messageText,
                                    isSentByMe = true,
                                    timestamp = currentTime
                                )
                            )
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF2196F3), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatMessageItem(message)
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (message.isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (message.isSentByMe) Color(0xFF2196F3) else Color.White
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = if (message.isSentByMe) Color.White else Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = message.timestamp,
                    color = if (message.isSentByMe) Color(0xFFD1E8FF) else Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}