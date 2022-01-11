package com.easyO.chatclone_u.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.FragmentMainBinding
import com.easyO.chatclone_u.model.User
import com.easyO.chatclone_u.repository.OtherUserRepository
import com.easyO.chatclone_u.util.ApiResponse
import com.easyO.chatclone_u.util.FireDataUtil
import com.easyO.chatclone_u.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {
    private lateinit var binder: FragmentMainBinding
    private var userIdList = ArrayList<String>()
    private var currentWatchingUserUid : String? = null
    private val otherUserRepository = OtherUserRepository()
    private lateinit var getOtherUserIdFlow: Flow<ApiResponse<ArrayList<String>>>
    private var userData: User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_main, container, false)
        binder = FragmentMainBinding.bind(layoutInflater)

        Log.d("MainFragment", "MainFragment is created")

        // 다른 사용자 프로필을 가져와서 게시한다
        CoroutineScope(Dispatchers.IO).launch {
            // 모든 유저 데이터 가져오기
            getOtherUserIdFlow = otherUserRepository.getUsersId()

            getOtherUsersData()
        }

        binder.dislikeButton.setOnClickListener {
            getUserData()
            // 사용자가 프로필을 설정한 상태라면 이 부분은 하지 않는다
            if (!AppClass.hasUserInfo) {
                // 프로필일 등록하지 않았을 때는 매칭 기능을 사용할 수 없다
                if (userData?.name == "" || userData?.name == null){
                    requireContext().showToast("please set your profile first")
                    return@setOnClickListener
                }
            }else{
                // 프로필 등록을 마친 상태라면 -> 현재 보고있는 유저는 유저 리스트에서 없애고 -> 다른 채팅 대상 가져오기
                CoroutineScope(Dispatchers.IO).launch {
                    getOtherUsersData()
                }
            }
        }

        binder.likeButton.setOnClickListener {
            getUserData()
            // 사용자가 프로필을 설정한 상태라면 이 부분은 하지 않는다
            if (!AppClass.hasUserInfo) {
                // 프로필일 등록하지 않았을 때는 매칭 기능을 사용할 수 없다
                if (userData?.name == "" || userData?.name == null){
                    requireContext().showToast("please set your profile first")
                    Log.d("MainFragment", "please set your profile first")
                    return@setOnClickListener
                }
            }else{
                // 친구 정보를 DB에 등록한다
                requireContext().showToast("Added new Friend!!")
                FireDataUtil.registerFriend(currentWatchingUserUid!!)
            }
        }

        return layoutInflater
    }

    // 현재 서버에 있는 사용자 최신 데이터를 가져온다
    private fun getUserData(){
        if (userData == null){
            userData = FireDataUtil.getUerData()
        }
    }

    // 가져온 모든 유저 uidList를 바탕으로 다른 유저 데이터를 1개씩 가져온다
    private suspend fun getOtherUsersData(){
        // 현재 mainFragment에 표시되고 있는 유저는 리스트에서 삭제한다
        if (currentWatchingUserUid != null){
            userIdList.remove(currentWatchingUserUid)
        }

        getOtherUserIdFlow.collect {
            when(it){
                is ApiResponse.Success -> {
                    if (userIdList.size == 0){
                        userIdList.addAll(it.data!!)
                        userIdList.remove(AppClass.currentUser!!.uid)
                    }

                    Log.d("MainFragment", "my uid: ${AppClass.currentUser!!.uid}")
//                    Log.d("MainFragment", "userID: ${it.data}")

                    Log.d("MainFragment", "userList: $userIdList")

                    val random = Random()
                    val listSize = userIdList.size
                    val randomNumber = random.nextInt(listSize)

                    // 현재 보고 있는 유저 id를 일시 보관
                    currentWatchingUserUid = userIdList[randomNumber]

                    // 다른 유저의 데이터만 가져오기
                    val getOtherUserDataFlow = otherUserRepository.getOtherUserData(currentWatchingUserUid!!)

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
                    val getOtherUserPictureFlow = otherUserRepository.getUserPicture(userIdList[randomNumber])
                    getOtherUserPictureFlow.collect {
                        when(it){
                            is ApiResponse.Success -> {
                                coroutineScope { launch(Dispatchers.Main) {
                                    binder.profileImageView.setImageBitmap(it.data)
                                    binder.progressBar.isGone = true
                                } }
                            }
                            is ApiResponse.Loading -> {
                                coroutineScope { launch(Dispatchers.Main) {
                                    binder.progressBar.isGone = false
                                } }
                            }
                            is ApiResponse.Error -> {
                                Log.d("MainFragment", "${it.message}")
                            }
                        }
                    }
                }
                is ApiResponse.Loading -> {

                }
                is ApiResponse.Error -> {
                    Log.d("MainFragment", "get other use data is failed")
                }
            }
        }
    }
}