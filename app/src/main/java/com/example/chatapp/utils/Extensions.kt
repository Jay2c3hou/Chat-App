package com.example.chatapp.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast

fun EditText.textToString() = this.text.toString()
fun EditText.textToStringAndTrim() = this.text.toString().trim()

fun Context.showToastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}