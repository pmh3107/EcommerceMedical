package app.ecommercemedical.viewmodel//import android.annotation.SuppressLint
//import android.icu.text.SimpleDateFormat
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import app.ecommercemedical.data.model.ChatMessage
//import app.ecommercemedical.data.repository.ChatRepository
//import kotlinx.coroutines.launch
//import java.util.Date
//
//
//class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
//    private val _messages = MutableLiveData<List<ChatMessage>>()
//    val messages: LiveData<List<ChatMessage>> get() = _messages
//
//    @SuppressLint("SimpleDateFormat")
//    fun sendMessage(text: String) {
//        val currentTime = SimpleDateFormat("hh:mm").format(Date())
//        val newMessages = _messages.value?.toMutableList() ?: mutableListOf()
//
//        viewModelScope.launch {
//            try {
//                val aiResponse = repository.getAiResponse(text)
//                newMessages.add(ChatMessage(newMessages.size + 1, text, true, currentTime))
//                _messages.value = newMessages
//                val aiMessage = ChatMessage(newMessages.size + 1, aiResponse, false, currentTime)
//                newMessages.add(aiMessage)
//                _messages.value = newMessages
//            } catch (e: Exception) {
//                Log.e("CHAT AI", "ERROR: $e")
//            }
//        }
//    }
//}
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import app.ecommercemedical.data.model.ChatMessage
import app.ecommercemedical.data.repository.ChatRepository
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.getValue
import java.text.SimpleDateFormat
import java.util.Date

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> get() = _messages

    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages")

    init {
//        messagesRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val messagesList = mutableListOf<ChatMessage>()
//                for (child in snapshot.children) {
//                    try {
//                        val message = child.getValue(ChatMessage::class.java)
//                        if (message != null) {
//                            message.id = child.key!!
//                            messagesList.add(message)
//                        }
//                    } catch (e: DatabaseException) {
//                        Log.e("ChatViewModel", "Error mapping message ${child.key}: $e")
//                    }
//                }
//                _messages.value = messagesList
//                val value = snapshot.getValue<String>()
//                Log.d(TAG, "Value is: " + value)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                println("Error MESSAGE: $error")
//            }
//        })
    }


    fun sendMessage(text: String, sender: String) {
        val currentTime = SimpleDateFormat("hh:mm").format(Date())
        repository.sendMessage(text, sender, currentTime)
    }
}