package com.example.chatapp.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.databinding.ItemContainerUserBinding
import com.example.chatapp.listeners.UserListener
import com.example.chatapp.models.User
import com.example.chatapp.utils.MyLog

class UserAdapter : Adapter<UserAdapter.UserViewHolder>() {

    private var users = emptyList<User>()

    private var userListener: UserListener? = null
    private fun getUserImage(encodedImage: String): Bitmap {
        /*解码  Base64 编码是一种将二进制数据转换成 ASCII 字符串的编码方式*/
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContainerUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        MyLog.v("这个是users的长度${users.size}")
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(users[position]) {

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<User>) {
        users = data
        notifyDataSetChanged()
    }

    fun setUserListener(listener: UserListener) {
        userListener = listener
    }

    inner class UserViewHolder(private val binding: ItemContainerUserBinding) :
        ViewHolder(binding.root) {
        fun setUserData(user: User, callback: () -> Unit = {}) {
            binding.textName.text = user.name
            binding.textEmail.text = user.email
            binding.imageProfile.setImageBitmap(getUserImage(user.image))
            binding.root.setOnClickListener { userListener?.onUserClicked(user) }
            callback()
        }
    }
}