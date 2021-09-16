package com.easyO.chatclone_u

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyO.chatclone_u.adapter.UserAdapter
import com.easyO.chatclone_u.databinding.FragmentMainBinding
import com.easyO.chatclone_u.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainFragment : Fragment() {
    private lateinit var binder : FragmentMainBinding
    private lateinit var databaseRef : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_main, container, false)
        binder = FragmentMainBinding.bind(layoutInflater)

        // firebase 초기화
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference()

        val userList = ArrayList<User>()

        // 리사이클러뷰 세팅 with User 클래스
        val userAdapter = UserAdapter(userList)
        binder.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binder.recyclerView.adapter = userAdapter

        // 데이터 베이스의 user에 있는 값이 변경 되었을 때
        databaseRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                snapshot.children.forEach {
                    val userData = it.getValue(User::class.java)

                    userList.add(userData!!)
                }

                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return layoutInflater
    }
}