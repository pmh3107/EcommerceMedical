package app.ecommercemedical.data.repository

//import com.google.firebase.Firebase
//import com.google.firebase.vertexai.vertexAI
//
//class ChatRepository {
//    private val generativeModel = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
//
//    suspend fun getAiResponse(prompt: String): String {
//        return try {
//            val response = generativeModel.generateContent(prompt)
//            response.text ?: ""
//        } catch (e: Exception) {
//            return "Error chat with AI: ${e.message}"
//        }
//    }
//}

import app.ecommercemedical.data.model.ChatMessage
import com.google.firebase.database.FirebaseDatabase

class ChatRepository {
    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages")

    fun sendMessage(text: String, sender: String, timestamp: String) {
        val key = messagesRef.push().key
        if (key != null) {
            val message = ChatMessage(key, text, sender, timestamp)
            messagesRef.child(key).setValue(message)
        }
    }
}