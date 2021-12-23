package com.easyO.chatclone_u.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.adapter.MessageAdapter
import com.easyO.chatclone_u.databinding.ActivityChatBinding
import com.easyO.chatclone_u.util.SendMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binder : ActivityChatBinding
    private lateinit var messageDbRef : DatabaseReference
    private var messageList = ArrayList<SendMessage>()

    // 각 room은 대화방의 고유 코드를 의미한다
    //
    var receiverRoom : String? = null
    var senderRoom : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid

        messageDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        val messageAdapter = MessageAdapter(messageList)
        binder.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binder.chatRecyclerView.adapter = messageAdapter

        // 리사이클러뷰에 주고 받은 메시지 데이터를 넣는다
        // addValueEventListener를 넣어 firebase의 데이터 베이스가 바뀔 때마다 리사이클러뷰를 갱신하게 한다
        messageDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    snapshot.children.forEach {
                        val message = it.getValue(SendMessage::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", "message Load canceled")
                }
            })

        // 보내기 버튼 클릭 시 메시지를 데이터베이스에 저장한다
        binder.sentMessageImageView.setOnClickListener {
            val message = binder.messageEditText.text.toString()
            val messageObject = SendMessage(message, senderUid)

            // 메시지를 보내면 chat\messages에 데이터를 추가한다
            // 보낼 내용은 메시지 내용이랑 uid를 같이 송신한다
            messageDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    // 보내는게 성공하면 받는 쪽에도 메시지를 업데이트 한다.
                    messageDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binder.messageEditText.text = null
        }
    }
}