package com.easyO.chatclone_u.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.adapter.UserAdapter
import com.easyO.chatclone_u.databinding.FragmentTalkBinding
import com.easyO.chatclone_u.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TalkFragment : Fragment() {
    private lateinit var binder : FragmentTalkBinding
    private lateinit var databaseRef : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var valueChangeListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_talk, container, false)
        binder = FragmentTalkBinding.bind(layoutInflater)

        // firebase 초기화
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference()

        val userList = ArrayList<User>()
        val currentUserUid = auth.uid

        // 리사이클러뷰 세팅 with User 클래스
        val userAdapter = UserAdapter(requireContext(), userList)
        binder.talkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binder.talkRecyclerView.adapter = userAdapter

        // 데이터 베이스의 user에 있는 값이 변경 되었을 때
        valueChangeListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                snapshot.children.forEach {
                    val userData = it.getValue(User::class.java)
                    Log.d("TAG", "TalkFragment userData: ${userData}")
                    // 아이디를 firebase 콘솔에서 삭제한 경우 uid만 사라지고 해당 계정은 일정 기간 남아있음
                    // userData.uid != null가 없을 경우 이 남은거도 유저로 인식해버린다
                    if (currentUserUid != userData!!.uid && userData.uid != null){
                        userList.add(userData)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        return layoutInflater
    }

    override fun onPause() {
        super.onPause()
        databaseRef.child("user").removeEventListener(valueChangeListener)
        Log.d("TAG", "TalkFragment onPause")
    }

    override fun onResume() {
        super.onResume()
        databaseRef.child("user").addValueEventListener(valueChangeListener)
        Log.d("TAG", "TalkFragment onResume")
    }
}