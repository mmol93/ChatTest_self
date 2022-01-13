package com.easyO.chatclone_u

import android.app.Application
import com.google.firebase.auth.FirebaseUser

class AppClass : Application() {

    companion object {
        // 어느 클래스에서도 context를 쓸 수 있게 설정
        // 보통 Toast 같은거 표시할 때 많이 쓰임
        lateinit var context : AppClass
        var hasUserInfo = false

        var currentUser : FirebaseUser? = null
        val friends = ArrayList<String>()
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}