package com.example.chatapp.models

class ChatMessage(
    var senderId: String,
    var receiverId: String,
    var message: String,
    var dateTime: String
)