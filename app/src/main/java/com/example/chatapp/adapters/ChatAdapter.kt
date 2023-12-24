package com.example.chatapp.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.databinding.ItemContainerReceivedMessageBinding
import com.example.chatapp.databinding.ItemContainerSentMessageBinding
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.utils.MyLog


class ChatAdapter : Adapter<ViewHolder>() {

    private lateinit var receiverProfileImage: Bitmap
    private lateinit var chatMessages: List<ChatMessage>
    private lateinit var senderId: String
    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    fun setReceiverProfileImage(image: Bitmap) {
        receiverProfileImage = image
    }

    fun setChatMessage(message: List<ChatMessage>) {
        chatMessages = message
    }

    fun setSenderId(id: String) {
        senderId = id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_SENT) {
            return SentMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            return ReceivedMessageViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            holder as SentMessageViewHolder
            holder.setData(chatMessages[position]) {
                MyLog.v("${chatMessages[position]}")
            }
        } else {
            holder as ReceivedMessageViewHolder
            holder.setData(chatMessages[position], receiverProfileImage) {
                MyLog.v("${chatMessages[position]}")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].senderId == senderId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    inner class SentMessageViewHolder(private val binding: ItemContainerSentMessageBinding) :
        ViewHolder(binding.root) {
        fun setData(chatMessage: ChatMessage, callback: (ChatMessage) -> Unit) {
            binding.textMessage.text = chatMessage.message
            binding.textDateTime.text = chatMessage.dateTime
            callback(chatMessage)
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemContainerReceivedMessageBinding) :
        ViewHolder(binding.root) {
        fun setData(
            chatMessage: ChatMessage,
            receiverProfileImage: Bitmap,
            callback: (ChatMessage) -> Unit
        ) {
            binding.textMessage.text = chatMessage.message
            binding.textDateTime.text = chatMessage.dateTime
            binding.imageProfile.setImageBitmap(receiverProfileImage)
            callback(chatMessage)
        }
    }

}