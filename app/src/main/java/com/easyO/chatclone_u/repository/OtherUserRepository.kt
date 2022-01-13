package com.easyO.chatclone_u.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.model.User
import com.easyO.chatclone_u.util.ApiResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OtherUserRepository {
    private val auth = Firebase.auth

    // 모든 유저의 uid를 가져온다
    fun getUsersId() = flow<ApiResponse<ArrayList<String>>> {
        val users = ArrayList<String>()

        emit(ApiResponse.Loading())

        AppClass.currentUser = auth.currentUser
        FirebaseDatabase.getInstance().reference.child("user").get().addOnSuccessListener {
            for (otherUser in it.children) {
                users.add(otherUser.key.toString())
            }
            Log.d("OtherUserRepository", "users: $users")
        }.await()

        emit(ApiResponse.Success(users))
    }.catch {
        emit(ApiResponse.Error("Error: ${it.message}"))
    }

    // 해당 uid의 유저 정보를 가져온다
    fun getOtherUserData(userId: String) = flow<ApiResponse<User?>> {
        emit(ApiResponse.Loading())

        var otherUserData: User? = null

        AppClass.currentUser = auth.currentUser
        FirebaseDatabase.getInstance().reference.child("user").child(userId).child("basic").get()
            .addOnSuccessListener {
                otherUserData = it.getValue(User::class.java)
            }.await()

        emit(ApiResponse.Success(otherUserData))

    }.catch {
        emit(ApiResponse.Error("Error: ${it.message}"))
    }

    // 해당 uid의 유저 프로필 사진을 가져온다
    fun getUserPicture(userId: String) = flow<ApiResponse<Bitmap?>> {
        var bitmap: Bitmap? = null
        emit(ApiResponse.Loading())

        // 각 뷰에 데이터 넣기 from firebase
        // 프로필의 이미지는 firebase에서 가져오기 - before testing
        val storage = Firebase.storage
        val firebaseStorageRef = storage.reference
        val firebaseImagesRef: StorageReference? = firebaseStorageRef.child("Users")
            .child(userId).child("profile.jpg")

        // firebaseStore에서 프로필 사진 다운로드 하기
        if (firebaseImagesRef != null) {
            val ONE_MEGABYTE: Long = 1024 * 1024
            firebaseImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // byteArray를 bitmap으로 변환
                bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            }.await()

            emit(ApiResponse.Success(bitmap))
        }
    }.catch {
        emit(ApiResponse.Error("other user's profile image load Error: ${it.message}"))
    }

    // 친구 정보 가져오기
    fun getFriendsData() = flow<ApiResponse<ArrayList<String>>> {
        emit(ApiResponse.Loading())
        val friends = ArrayList<String>()
        FirebaseDatabase.getInstance().reference.child("user").child(AppClass.currentUser!!.uid)
            .child("friends").get().addOnSuccessListener {
                for (friend in it.children){
                    friends.add(friend.key.toString())
                }
            }.await()

        emit(ApiResponse.Success(friends))
    }.catch {
        emit(ApiResponse.Error("get friends list Error: ${it.message}"))
    }
}