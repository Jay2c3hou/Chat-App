package com.example.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners(){
        binding.textCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext,SignUpActivity::class.java))
        }
    }


}