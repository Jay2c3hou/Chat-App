package com.example.chatapp.utils

import android.util.Log

object MyLog {
    private const val IS_RELEASE = false

    fun v(content: String, tag: String = "yysc") {
        if (!IS_RELEASE) {
            Log.v(tag, content)
        }
    }

    fun i(content: String, tag: String = "yysc") {
        if (!IS_RELEASE) {
            Log.v(tag, content)
        }
    }

    fun d(content: String, tag: String = "yysc") {
        if (!IS_RELEASE) {
            Log.v(tag, content)
        }
    }

    fun e(content: String, tag: String = "yysc") {
        if (!IS_RELEASE) {
            Log.v(tag, content)
        }
    }

    fun w(content: String, tag: String = "yysc") {
        if (!IS_RELEASE) {
            Log.v(tag, content)
        }
    }
}