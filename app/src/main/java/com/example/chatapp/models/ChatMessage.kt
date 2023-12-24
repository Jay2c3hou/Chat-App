package com.example.chatapp.models

import java.util.Date

class ChatMessage(
    var senderId: String,
    var receiverId: String,
    var message: String,
    var dateTime: String,
    var dateObject: Date,
    var conversionId: String,
    var conversionName: String,
    var conversionImage: String
)