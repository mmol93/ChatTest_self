package com.easyO.chatclone_u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.easyO.chatclone_u.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binder : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth

        // 이메일 버튼 클릭 시
        binder.emailButton.setOnClickListener {
            val emailLoginDialog = EmailLoginDialog(this)
            emailLoginDialog.show()
        }

    }

    override fun onStart() {
        super.onStart()
        // 현재 로그인 되어있는 상태인지 확인
        val currentUser = auth.currentUser
        if(currentUser != null){

        }
    }

}