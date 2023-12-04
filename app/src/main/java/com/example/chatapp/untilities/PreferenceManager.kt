package com.example.chatapp.untilities

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager() {
    private lateinit var sharedPreference: SharedPreferences

    constructor(context: Context) : this() {
        sharedPreference =
            context.getSharedPreferences(Constants().KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreference.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String) = sharedPreference.getBoolean(key, false)

    fun putString(key: String, value: String) {
        sharedPreference.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String) = sharedPreference.getString(key, null)

    fun clear() {
        sharedPreference.edit().apply {
//       所有通过 SharedPreferences 存储的键值对都会被移除。
            clear()
//      apply() 是异步的,不会阻塞主线程
            apply()
        }
    }
}