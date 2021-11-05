package com.easyO.chatclone_u.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.FragmentHomeBinding
import com.easyO.chatclone_u.util.FireDataUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binder : FragmentHomeBinding
    lateinit var currentUserDataRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_home, container, false)
        binder = FragmentHomeBinding.bind(layoutInflater)

        return layoutInflater
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder.findButton.setOnClickListener {
            // 불필요한거 Gone 처리
            binder.findButton.isGone = true
            binder.waveLottie.isGone = true

            // 로딩 화면
            binder.loadingLottie.isGone = false

            // 파이어베이스에서 데이터 가져오기 - 모든 유저의 uid 가져오기
            val dataListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (user in snapshot.children){
                            Log.d("FireDataUtil", "user: ${user.key}")
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            FireDataUtil.currentUserDataRef.addListenerForSingleValueEvent(dataListener)
        }
    }
}