package com.example.chatapp.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.RequiresApi
import com.example.chatapp.R
import com.example.chatapp.adapters.ChatAdapter
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User
import com.example.chatapp.untilities.Constants
import com.example.chatapp.untilities.PreferenceManager
import com.example.chatapp.utils.MyLog
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverUser: User
    private lateinit var chatMessages: List<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setListeners()
        loadReceiverDetails()
        setContentView(binding.root)
        init()
    }

    private fun init() {
        preferenceManager = PreferenceManager.getInstance(applicationContext)
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter().apply {
            setChatMessage(chatMessages)
            setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.image))
            setSenderId(preferenceManager.getString(Constants.KEY_USER_ID) ?: "errorId")
        }
        binding.chatRecyclerView.adapter = chatAdapter
        database = FirebaseFirestore.getInstance()
    }

    private fun sendMessage() {
        val message = HashMap<String, Any>()
        message.put(
            Constants.KEY_SENDER_ID,
            preferenceManager.getString(Constants.KEY_USER_ID) ?: "errorUserId"
        )
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id)
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.text.toString())
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
        binding.inputMessage.text = null
    }

    private fun getBitmapFromEncodedString(encodedImage: String): Bitmap {
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun loadReceiverDetails() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            receiverUser = intent.getSerializableExtra(Constants.KEY_USER, User::class.java)
                ?: User().also { MyLog.v("未能读取到用户信息") }
            binding.textName.text = receiverUser.name
        } else {
            receiverUser = intent.getSerializableExtra(Constants.KEY_USER) as User
            binding.textName.text = receiverUser.name
        }

    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.layoutSend.setOnClickListener {
            sendMessage()
        }
    }
}