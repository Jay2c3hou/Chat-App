package com.example.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignInBinding
import com.example.chatapp.untilities.Constants
import com.example.chatapp.untilities.PreferenceManager
import com.example.chatapp.utils.showToastShort
import com.example.chatapp.utils.textToString
import com.example.chatapp.utils.textToStringAndTrim
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager.getInstance(applicationContext)
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.textCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
        binding.buttonSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
                signIn()
            }
        }
    }

    private fun signIn() {
        loading(true)
        val database = FirebaseFirestore.getInstance()
        //这个就是我们远程的Firestore的表
        database.collection(Constants.KEY_COLLECTION_USERS)
//                查询与输入的邮箱和密码匹配的用户
            .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.textToString())
            .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.textToString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null
                    && task.result.documents.size > 0
                ) {
                    val documentSnapshot = task.result.documents[0]
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                    preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.id)
                    preferenceManager.putString(
                        Constants.KEY_NAME,
                        documentSnapshot.getString(Constants.KEY_NAME) ?: ""
                    )
                    preferenceManager.putString(
                        Constants.KEY_IMAGE,
                        documentSnapshot.getString(Constants.KEY_IMAGE) ?: ""
                    )
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{
                    loading(false)
                    showToastLong("登入失败,请检查用户信息")
                }
            }
    }

    private fun loading(isLoading: Boolean) {
        binding.buttonSignIn.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToastLong(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun isValidSignInDetails(): Boolean {
        return if (binding.inputEmail.textToStringAndTrim().isEmpty()) {
            showToastShort("请输入邮箱地址")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.textToString()).matches()) {
            showToastShort("无效邮箱地址")
            false
        } else if (binding.inputPassword.textToStringAndTrim().isEmpty()) {
            showToastShort("请输入密码")
            false
        } else true
    }

}