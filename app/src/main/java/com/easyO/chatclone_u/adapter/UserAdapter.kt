package com.easyO.chatclone_u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.UserItemBinding
import com.easyO.chatclone_u.model.User


class UserAdapter(val userList : ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.binder.idTextView.text = currentUser.email
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binder = UserItemBinding.bind(view)
}
