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
        currentUserDataRef.child("user").child(uid).child("basic").setValue(User(name = "", email = email, uid = uid))
    }

    // 유저 데이터 업데이트
    fun userDataUpdate(name:String?, sex:String?, age:String?, info:String?, uid:String){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        currentUserDataRef.child("user").child(uid).child("basic").child("name").setValue(name)
        currentUserDataRef.child("user").child(uid).child("basic").child("sex").setValue(sex)
        currentUserDataRef.child("user").child(uid).child("basic").child("age").setValue(age)
        currentUserDataRef.child("user").child(uid).child("basic").child("info").setValue(info)
    }

    // 유저 태그 업데이트
    fun userTagUpdate(uid: String, tag1: String?, tag2 : String?, tag3: String?, tag4: String?, tag5: String?){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()

        if (tag1 != null){
            currentUserDataRef.child("user").child(uid).child("tag").child("tag1").setValue(tag1)
        }
        if (tag2 != null){
            currentUserDataRef.child("user").child(uid).child("tag").child("tag2").setValue(tag2)
        }
        if (tag3 != null){
            currentUserDataRef.child("user").child(uid).child("tag").child("tag3").setValue(tag3)
        }
        if (tag4 != null){
            currentUserDataRef.child("user").child(uid).child("tag").child("tag4").setValue(tag4)
        }
        if (tag5 != null){
            currentUserDataRef.child("user").child(uid).child("tag").child("tag5").setValue(tag5)
        }
    }

    // 프로필 사진 갱신을 위한 데이터베이스 업데이트 - FireStorage.kt 참조
    fun triggerDataUpload(uid: String, data:String){
        currentUserDataRef = FirebaseDatabase.getInstance().getReference()
        currentUserDataRef.child("user").child(uid).child("trigger").setValue(data)
    }
}