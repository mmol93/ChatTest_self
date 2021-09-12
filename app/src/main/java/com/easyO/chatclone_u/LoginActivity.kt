package com.easyO.chatclone_u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.easyO.chatclone_u.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.internal.ContextUtils.getActivity
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
        // loginDialog 객체 생성
        val emailLoginDialog = EmailLoginDialog(this, this)

        // 이메일 버튼 클릭 시
        binder.emailButton.setOnClickListener {
            emailLoginDialog.show()
        }
        // loginDialog를 닫아서 LoginActivity로 돌아왔을 때
        emailLoginDialog.setOnDismissListener {
            // 현재 로그인 되어있는 상태인지 확인
            if(AppClass.currentUser != null){
                Toast.makeText(AppClass.context, "Login Success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(AppClass.context, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}