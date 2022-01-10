package com.easyO.chatclone_u.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.model.User
import com.easyO.chatclone_u.util.ApiResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OtherUserRepository {
    private val auth = Firebase.auth
    private var userData: User? = null

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

    fun getOtherUserData(userId: String) = flow<ApiResponse<User?>> {
        emit(ApiResponse.Loading())

        var result: User? = null

        AppClass.currentUser = auth.currentUser
        FirebaseDatabase.getInstance().reference.child("user").child(userId).child("basic").get()
            .addOnSuccessListener {
                result = it.getValue(User::class.java)
            }.await()

        emit(ApiResponse.Success(result))

    }.catch {
        emit(ApiResponse.Error("Error: ${it.message}"))
    }

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
}