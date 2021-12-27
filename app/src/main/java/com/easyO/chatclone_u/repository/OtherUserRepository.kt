package com.easyO.chatclone_u.repository

import android.graphics.Bitmap
import android.util.Log
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.model.User
import com.easyO.chatclone_u.util.ApiResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OtherUserRepository {
    private val auth = Firebase.auth
    private var userData : User? = null

    fun getUsersId() = flow<ApiResponse<ArrayList<String>>> {
        val users = ArrayList<String>()

        emit(ApiResponse.Loading())

        AppClass.currentUser = auth.currentUser
        FirebaseDatabase.getInstance().reference.child("user").get().addOnSuccessListener {
            for (otherUser in it.children){
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

        var result : User? = null

        AppClass.currentUser = auth.currentUser
        FirebaseDatabase.getInstance().reference.child("user").child(userId).child("basic").get().addOnSuccessListener {
            result = it.getValue(User::class.java)
        }.await()

        emit(ApiResponse.Success(result))

    }.catch {
        emit(ApiResponse.Error("Error: ${it.message}"))
    }

    fun getUserPicture(userId: String) = flow<Bitmap?> {

    }
}