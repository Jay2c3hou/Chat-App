package com.example.chatapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.RequiresApi
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.models.User
import com.example.chatapp.untilities.Constants
import com.example.chatapp.utils.MyLog

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setListeners()
        loadReceiverDetails()
        setContentView(binding.root)
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
    }
}