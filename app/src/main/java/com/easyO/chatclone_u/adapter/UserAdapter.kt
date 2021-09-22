package com.easyO.chatclone_u.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyO.chatclone_u.ChatActivity
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.UserItemBinding
import com.easyO.chatclone_u.model.User
import com.google.firebase.auth.FirebaseAuth


class UserAdapter(val context : Context, val userList : ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userList[position]
        // 현재 유저 표시
        holder.binder.idTextView.text = currentUser.email

        // 유저 item_row 클릭 시 대화방 만들게 하기
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binder = UserItemBinding.bind(view)
}
