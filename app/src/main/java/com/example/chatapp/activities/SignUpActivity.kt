package com.example.chatapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.example.chatapp.untilities.Constants
import com.example.chatapp.untilities.PreferenceManager
import com.example.chatapp.utils.MyLog
import com.example.chatapp.utils.showToastLong
import com.example.chatapp.utils.showToastShort
import com.example.chatapp.utils.textToString
import com.example.chatapp.utils.textToStringAndTrim
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var preferenceManager: PreferenceManager
    private var encodedImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager.getInstance(applicationContext)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.textSignIn.setOnClickListener {
//默认情况下，onBackPressed()方法会执行Activity的finish()方法，关闭当前Activity并返回上一个Activity
            onBackPressedDispatcher.onBackPressed()
        }
        binding.buttonSignUp.setOnClickListener {
            if (isValidSignUpDetails()) {
                signUp()
            }
        }
        binding.layoutImage.setOnClickListener {
//      一般使用 ACTION_PICK 选择图片
//      MediaStore.Images.Media 是系统图库的 URI，告诉 Intent 我们想要选择外部存储上的图片
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//      临时访问读权限 intent的接受者将被授予 INTENT 数据uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private fun signUp() {
        loading(true)
        val database = FirebaseFirestore.getInstance()
        val user = HashMap<String, Any>()
        user.put(Constants.KEY_NAME, binding.inputName.textToString())
        user.put(Constants.KEY_EMAIL, binding.inputEmail.textToString())
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.textToString())
        user.put(Constants.KEY_IMAGE, encodedImage)
        database.collection(Constants.KEY_COLLECTION_USERS)
            .add(user)
            .addOnSuccessListener {
                MyLog.v("---------Success---------")
                loading(false)
                MyLog.v("${binding.progressBar.isVisible}")
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID, it.id)
                preferenceManager.putString(Constants.KEY_NAME, binding.inputName.textToString())
                preferenceManager.putString(Constants.KEY_IMAGE, encodedImage)
                val intent = Intent(applicationContext, MainActivity::class.java)
//                新启一个 并清空任务栈
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                MyLog.v("---------Failure---------")
                loading(false)
                Log.v("yysc", "$it")
                showToastLong(it.message ?: "未知错误")
            }
    }

    @SuppressLint("RestrictedApi")
    private fun encodeImage(bitmap: Bitmap): String {
        val previewWidth = dpToPx(applicationContext, 150).toInt()
//        根据原始图片的宽高比 来计算缩略图的高度
        val previewHeight = bitmap.height * previewWidth / bitmap.width
//        false表示不应用滤镜
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val bos = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bytes = bos.toByteArray()
//      Bitmap对象编码为一个经过压缩和缩放的JPEG格式的Base64编码字符串
//      Base64 字符串可以用于网络传输或存储，因为它已经是文本格式，不受二进制数据可能遇到的问题
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private val pickImage = registerForActivityResult(
        //StartActivityForResult 用于启动任意意图（Intent）,并且期望从那个 Activity 获取结果
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
//第一个data是ActivityResult 类型的 result 对象的属性,表示从返回的 Activity 中获取的 Intent 对象，包含了所有返回的数据
//第二个data 是 Intent 类型对象的属性
                val imageUri = result.data?.data
                try {
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imageProfile.setImageBitmap(bitmap)
                    binding.textAddImage.visibility = View.GONE
                    encodedImage = encodeImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isValidSignUpDetails(): Boolean {
        return if (encodedImage.isEmpty()) {
            showToastShort("请选择头像")
            false
        } else if (binding.inputName.textToStringAndTrim().isEmpty()) {
            showToastShort("请输入名字")
            false
        } else if (binding.inputEmail.textToStringAndTrim().isEmpty()) {
            showToastShort("请输入邮箱")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.textToString()).matches()) {
            showToastLong("请输入有效的邮箱地址")
            false
        } else if (binding.inputPassword.textToStringAndTrim().isEmpty()) {
            showToastShort("请输入密码")
            false
        } else if (binding.inputConfirmPassword.textToStringAndTrim().isEmpty()) {
            showToastShort("请确认密码")
            false
        } else if (binding.inputPassword.textToString() != binding.inputConfirmPassword.textToString()
        ) {
            showToastShort("两次输入密码不同")
            false
        } else true
    }

    private fun loading(isLoading: Boolean) {
        binding.buttonSignUp.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}