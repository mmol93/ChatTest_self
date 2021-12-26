package com.easyO.chatclone_u.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.FragmentMainBinding
import com.easyO.chatclone_u.util.FireDataUtil
import com.easyO.chatclone_u.util.showToast

class MainFragment : Fragment() {
    private lateinit var binder: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_main, container, false)
        binder = FragmentMainBinding.bind(layoutInflater)

        Log.d("MainFragment", "MainFragment is created")

        binder.dislikeButton.setOnClickListener {
            // 사용자가 프로필을 설정한 상태라면 이 부분은 하지 않는다
            if (!AppClass.hasUserInfo) {
                val userData = FireDataUtil.getUerData()
                // 프로필일 등록하지 않았을 때는 매칭 기능을 사용할 수 없다
                if (userData?.name == null){
                    requireContext().showToast("please set your profile first")
                    return@setOnClickListener
                }
            }
        }

        binder.likeButton.setOnClickListener {
            // 사용자가 프로필을 설정한 상태라면 이 부분은 하지 않는다
            if (!AppClass.hasUserInfo) {
                val userData = FireDataUtil.getUerData()
                // 프로필일 등록하지 않았을 때는 매칭 기능을 사용할 수 없다
                if (userData?.name == null){
                    requireContext().showToast("please set your profile first")
                    return@setOnClickListener
                }
            }
        }

        return layoutInflater
    }
}