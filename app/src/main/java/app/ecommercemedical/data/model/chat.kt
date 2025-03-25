package app.ecommercemedical.data.model

data class ChatMessage(
    var id: String = "",
    val text: String,
    val sender: String,
    val timestamp: String
)
