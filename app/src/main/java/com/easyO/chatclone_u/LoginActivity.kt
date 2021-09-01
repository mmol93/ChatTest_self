package com.easyO.chatclone_u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // 현재 로그인 되어있는 상태인지 확인
        val currentUser = auth.currentUser
        if(currentUser != null){

        }
    }

}