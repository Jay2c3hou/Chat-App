package com.example.chatapp.utils

import android.widget.EditText

fun EditText.textToString() = this.text.toString()
fun EditText.textToStringAndTrim() = this.text.toString().trim()