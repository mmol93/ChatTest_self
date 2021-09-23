package com.easyO.chatclone_u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.RecevieBinding
import com.easyO.chatclone_u.databinding.SentBinding
import com.easyO.chatclone_u.util.SendMessage
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val messageList : ArrayList<SendMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // override fun getItemViewType에서 반환한 값에 따라 다른 viewHolder를 return 하게 한다
        if (viewType == 1){
            // return ReceiveMessageViewHolder
            return ReceiveMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recevie, parent, false))
        }else{
            // return SendMessageViewHolder
            return SendMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sent, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        // 호출된게 SendMessageViewHolder 즉, 보낸 메시지에 의한거면
        if (holder.javaClass == SendMessageViewHolder::class.java){
            val viewHolder = holder as SendMessageViewHolder

            viewHolder.binder.sentMessageButton.text = currentMessage.message
        }
        // 호출된게 ReceiveMessageViewHolder 즉, 받는 메시지에 의한거면
        else{
            val viewHolder = holder as ReceiveMessageViewHolder

            viewHolder.binder.receiveMessageButton.text = currentMessage.message
        }
    }

    // ItemType를 지정하여 각기 다른 경우에 따라 반환할 viewHolder를 정할 수 있다.
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.senderId){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}

class SendMessageViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val binder = SentBinding.bind(view)
}

class ReceiveMessageViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val binder = RecevieBinding.bind(view)
}

