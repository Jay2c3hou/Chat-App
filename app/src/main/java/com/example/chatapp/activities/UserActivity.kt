package com.example.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatapp.R
import com.example.chatapp.adapters.UserAdapter
import com.example.chatapp.databinding.ActivityUserBinding
import com.example.chatapp.models.User
import com.example.chatapp.untilities.Constants
import com.example.chatapp.untilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        preferenceManager = PreferenceManager.getInstance(applicationContext)
        setContentView(binding.root)
        setListeners()
        getUser()
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getUser() {
        loading(true)
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                loading(false)
                val currentUserId = preferenceManager.getString(Constants.KEY_USER_ID) ?: "errorId"
                if (task.isSuccessful && task.result != null) {
                    val users = ArrayList<User>()
                    for (queryDocumentSnapshot in task.result) {
//                       如果其 ID 不等于当前用户的 ID，则创建一个新的 User 对象
                        if (currentUserId == queryDocumentSnapshot.id) continue
                        val user = User()
                        user.name =
                            queryDocumentSnapshot.getString(Constants.KEY_NAME) ?: "errorName"
                        user.email =
                            queryDocumentSnapshot.getString(Constants.KEY_EMAIL) ?: "errorEmail"
                        user.image =
                            queryDocumentSnapshot.getString(Constants.KEY_IMAGE) ?: "errorImage"
                        user.token =
                            queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN) ?: "errorToken"
                        users.add(user)
                    }
                    if (users.size > 0) {
                        val userAdapter = UserAdapter(users)
                        binding.userRecyclerView.adapter = userAdapter
                        binding.userRecyclerView.visibility = View.VISIBLE
                    } else
                        showErrorMessage()
                } else
                    showErrorMessage()
            }
    }

    private fun showErrorMessage() {
        binding.textErrorMessage.text = String.format("%s", "没有可用的用户")
        binding.textErrorMessage.visibility = View.VISIBLE
    }

    private fun loading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}