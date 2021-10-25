package com.easyO.chatclone_u.util

import com.easyO.chatclone_u.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.ktx.storage
import java.io.File

object FireDataUtil {
    lateinit var currentUserDataRef : DatabaseReference

    // 회원가입 시 유저 정보를 데이터베이스에 등록한다
    fun addUserToDatabase(email : String, uid : String){
        // 데이터 베이스 객체 생성
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        // 첫 등록의 경우 이름과 프로필 사진이 없기 때문에 이 둘은 null로 설정한다
        currentUserDataRef.child("user").child(uid).setValue(User(name = "", email, profilePicture = "", uid = uid))
    }

    // 유저 데이터 업데이트
    fun userDataUpdate(name:String?, sex:String?, age:String?, info:String?, uid:String){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        currentUserDataRef.child("user").child(uid).setValue(User(name = name, sex = sex, age = age, info = info))
    }

    // 유저 태그 업데이트
    fun userTagUpdate(uid: String, tag1: String?, tag2 : String?, tag3: String?, tag4: String?, tag5: String?){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        if (tag1 != null){
            currentUserDataRef.child("user").child(uid).child("tag1").setValue(tag1)
        }
        if (tag2 != null){
            currentUserDataRef.child("user").child(uid).setValue(User(tag2 = tag2))
        }
        if (tag3 != null){
            currentUserDataRef.child("user").child(uid).setValue(User(tag3 = tag3))
        }
        if (tag4 != null){
            currentUserDataRef.child("user").child(uid).setValue(User(tag4 = tag4))
        }
        if (tag5 != null){
            currentUserDataRef.child("user").child(uid).setValue(User(tag5 = tag5))
        }
    }

}