package com.example.chatapp.firebase

import com.example.chatapp.utils.MyLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        MyLog.v(tag = "FCM", content = token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        MyLog.v(tag = "FCM", content = "Message: ${message.notification?.body}")
    }

}