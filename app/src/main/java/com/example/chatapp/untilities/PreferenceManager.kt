package com.example.chatapp.untilities

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.lang.ref.WeakReference

class PreferenceManager private constructor() {
    private lateinit var weakContext: WeakReference<Context>
    private val sp: SharedPreferences by lazy {
        weakContext.get()!!
            .getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private var instance: PreferenceManager? = null
        fun getInstance(context: Context): PreferenceManager {
            return instance ?: PreferenceManager().also {
                instance = it
                it.weakContext = WeakReference(context)
            }
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        sp.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String) = sp.getBoolean(key, false)

    fun putString(key: String, value: String) {
        sp.edit {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String) = sp.getString(key, null)

    fun clear() {
        sp.edit {
//       所有通过 SharedPreferences 存储的键值对都会被移除。
            clear()
//      apply() 是异步的,不会阻塞主线程
            apply()
        }
    }
}