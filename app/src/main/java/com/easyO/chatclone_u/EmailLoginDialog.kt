package com.easyO.chatclone_u

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.easyO.chatclone_u.databinding.EmailLoginDialogBinding
import com.easyO.chatclone_u.util.FireDataUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class EmailLoginDialog(context: Context, private val activity:Activity) : Dialog(context) {
    private lateinit var binder : EmailLoginDialogBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 각종 초기화
        binder = EmailLoginDialogBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binder.root)

        // 배경을 투명하게 만들기
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // *** 로그인 버튼 기능 정의
        binder.loginButton.setOnClickListener {
            val email = binder.emailEditText.text.toString()
            val password = binder.passwordEditText.text.toString()

            // 이메일 형식의 이메일을 입력 & 패스워드는 6글자 이상 이어야함
            if (checkEmailFormat(email) && password.length >= 6){
                Log.d("TAG", "email: $email")
                Log.d("TAG", "password: $password")

                // Firebase에 로그인 시도(비동기로 실시됨)
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail: success")
                            AppClass.currentUser = auth.currentUser

                            // 그리고 editText 부분 전부 clear 하기
                            binder.emailEditText.text = null
                            binder.passwordEditText.text = null

                            // loginDialog 종료
                            this.dismiss()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(AppClass.context, "Email or Password is wrong.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

            }else if(!checkEmailFormat(email)){
                Toast.makeText(AppClass.context, "please set right email format", Toast.LENGTH_LONG).show()
            }else if(password.isEmpty()){
                Toast.makeText(AppClass.context, "please enter password", Toast.LENGTH_LONG).show()
            }else if(password.length < 6){
                Toast.makeText(AppClass.context, "Number of Passwords should be over than 5", Toast.LENGTH_LONG).show()
            }
        }

        // *** 회원가입 버튼 기능 정의
        binder.createButton.setOnClickListener {
            val email = binder.emailEditText.text.toString()
            val password = binder.passwordEditText.text.toString()

            // 이메일 형식의 이메일을 입력 & 패스워드는 6글자 이상 이어야함
            if (checkEmailFormat(email) && password.length >= 6){
                Log.d("TAG", "email: $email")
                Log.d("TAG", "password: $password")

                // Firebase에 회원가입
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(AppClass.context, "Authentication Success.",
                                Toast.LENGTH_SHORT).show()

                            // 회원 가입 성공했으면 해당 유저 정보를 데이터베이스에 등록하기
                            FireDataUtil.addUserToDatabase(email, user!!.uid)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(AppClass.context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                // 계정 생성 성공 시 키보드 내리게 하기
                val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binder.emailEditText.windowToken, 0)
                // 그리고 editText 부분 전부 clear 하기
                binder.emailEditText.text = null
                binder.passwordEditText.text = null

            }else if(!checkEmailFormat(email)){
                Toast.makeText(AppClass.context, "please set right email format", Toast.LENGTH_LONG).show()
            }else if(password.isEmpty()){
                Toast.makeText(AppClass.context, "please enter password", Toast.LENGTH_LONG).show()
            }else if(password.length < 6){
                Toast.makeText(AppClass.context, "Number of Passwords should be over than 5", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun checkEmailFormat(email:String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}