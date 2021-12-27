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
import com.easyO.chatclone_u.repository.OtherUserRepository
import com.easyO.chatclone_u.util.ApiResponse
import com.easyO.chatclone_u.util.FireDataUtil
import com.easyO.chatclone_u.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {
    private lateinit var binder: FragmentMainBinding
    private var userIdList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_main, container, false)
        binder = FragmentMainBinding.bind(layoutInflater)

        Log.d("MainFragment", "MainFragment is created")

        // 다른 사용자 프로필을 가져와서 게시한다
        CoroutineScope(Dispatchers.IO).launch {
            getOtherUsersData()
        }

        binder.dislikeButton.setOnClickListener {
            val userData = FireDataUtil.getUerData()
            // 사용자가 프로필을 설정한 상태라면 이 부분은 하지 않는다
            if (!AppClass.hasUserInfo) {
                // 프로필일 등록하지 않았을 때는 매칭 기능을 사용할 수 없다
                if (userData?.name == "" || userData?.name == null){
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
                if (userData?.name == "" || userData?.name == null){
                    requireContext().showToast("please set your profile first")
                    Log.d("MainFragment", "please set your profile first")
                    return@setOnClickListener
                }
            }
        }

        return layoutInflater
    }
    private suspend fun getOtherUsersData(){
        val otherUserRepository = OtherUserRepository()
        val getOtherUserIdFlow = otherUserRepository.getUsersId()

        getOtherUserIdFlow.collect {
            when(it){
                is ApiResponse.Success -> {
                    Log.d("MainFragment", "userID: ${it.data}")
                    userIdList.addAll(it.data!!)

                    // todo 자기 자신의 id는 제외한다
                    userIdList.remove(AppClass.currentUser!!.uid)

                    val random = Random()
                    val listSize = userIdList.size
                    val randomNumber = random.nextInt(listSize)

                    // 다른 유저의 데이터만 가져오기
                    val getOtherUserDataFlow = otherUserRepository.getOtherUserData(it.data[randomNumber])
                    getOtherUserDataFlow.collect {
                        when(it){
                            is ApiResponse.Success -> {
                                Log.d("MainFragment", "userData: ${it.data}")
                                coroutineScope { launch(Dispatchers.Main) {
                                    binder.nameTextView.text = it.data!!.name
                                    binder.ageTextview.text = "Age: ${it.data.age}"
                                    binder.introduceTextview.text = it.data.info
                                    binder.sexTextView.text = it.data.sex
                                } }
                            }
                        }
                    }

                    // 그 유저의 사진 데이터도 가져오기

                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Error -> {

                }
            }
        }
    }
    private suspend fun getAnotherUserData(){

    }

}