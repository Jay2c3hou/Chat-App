package com.example.chatapp.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.untilities.Constants
import com.example.chatapp.untilities.PreferenceManager
import com.example.chatapp.utils.showToastShort
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        preferenceManager = PreferenceManager.getInstance(applicationContext)
        setContentView(binding.root)
        loadUserDetails()
        getToken()
        setListeners()
    }

    private fun setListeners() {
        binding.imageSignOut.setOnClickListener {
            signOut()
        }
        binding.fabNewChat.setOnClickListener {
            startActivity(Intent(applicationContext, UserActivity::class.java))
        }
    }

    private fun loadUserDetails() {
        binding.textName.text = preferenceManager.getString(Constants.KEY_NAME)
        val bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.imageProfile.setImageBitmap(bitmap)
    }

    /*获取token 并更新*/
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    private fun updateToken(token: String) {
        val database = FirebaseFirestore.getInstance()
        /*collection()访问指定名称的集合*/
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            /*指定一个特定的文档*/
            preferenceManager.getString(Constants.KEY_USER_ID) ?: "defaultUserId"
        )
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnFailureListener { showToastShort("无法更新令牌 unable updated token") }
    }

    private fun signOut() {
        showToastShort("正在注销...")
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USER_ID) ?: "defaultUserId"
        )
        val updates = HashMap<String, Any>()
        /*删除用户文档中的 Constants.KEY_FCM_TOKEN 字段及其对应的数值。*/
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete())
        documentReference.update(updates)

            .addOnSuccessListener {
                preferenceManager.clear()
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener { showToastShort("注销失败") }
    }
}