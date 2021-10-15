package com.easyO.chatclone_u.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.easyO.chatclone_u.chat.ChatActivity
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.UserItemBinding
import com.easyO.chatclone_u.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserAdapter(val context : Context, val userList : ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    private val dialogList = arrayOf(context.getString(R.string.delete))
    private lateinit var messageDbRef : DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        messageDbRef = FirebaseDatabase.getInstance().reference

        val antherUser = userList[position]
        // 현재 유저 표시
        holder.binder.idTextView.text = antherUser.email

        // 유저 item_row 클릭 시 대화방 만들게 하기
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", antherUser.name)
            intent.putExtra("uid", antherUser.uid)

            context.startActivity(intent)
        }

        // 길게 클릭 시 dialog 나오게 하기
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(userList[position].email)
            builder.setNegativeButton("Cancel", null)

            // 긴 클릭에 대한 리스너 설정
            val longClickedListener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    // delete 클릭 시 - 해당 톡방 메시지 모두 삭제
                    if (which == 0){
                        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid
                        val receiverUid = antherUser.uid
                        val senderRoom = receiverUid + senderUid

                        messageDbRef.child("chats").child(senderRoom).removeValue()
                    }
                }
            }
            builder.setItems(dialogList, longClickedListener)
            builder.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binder = UserItemBinding.bind(view)
}
