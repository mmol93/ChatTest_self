package com.easyO.chatclone_u.util

import com.easyO.chatclone_u.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreUtil {
    // 초기화를 나중에 하기 위해 by lazy 사용
    private val fireStoreInstance : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    lateinit var currentUserDataRef : DatabaseReference

    // 회원가입 시 유저 정보를 데이터베이스에 등록한다
    fun addUserToDatabase(email : String, uid : String){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        // 첫 등록의 경우 이름과 프로필 사진이 없기 때문에 이 둘은 null로 설정한다
        currentUserDataRef.child("user").child(uid).setValue(User(name = "", email, profilePicture = "", uid = uid))
    }
}