package com.example.chatapp.listeners

import com.example.chatapp.models.User


interface UserListener {
    fun onUserClicked(user:User)
}