package com.easyO.chatclone_u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.easyO.chatclone_u.databinding.ActivityLoginBinding
import com.easyO.chatclone_u.util.FireDataUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binder : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleAccount : GoogleSignInAccount

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth
        // loginDialog 객체 생성
        val emailLoginDialog = EmailLoginDialog(this, this)

        // 구글 로그인에 필요한 객체 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // *** 구글 로그인 버튼 클릭 시
        binder.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // *** 이메일 버튼 클릭 시
        binder.emailButton.setOnClickListener {
            emailLoginDialog.show()
        }
        // *** loginDialog를 닫아서 LoginActivity로 돌아왔을 때
        emailLoginDialog.setOnDismissListener {
            // 현재 로그인 되어있는 상태인지 확인
            if(AppClass.currentUser != null){
                Toast.makeText(AppClass.context, "Login Success", Toast.LENGTH_SHORT).show()

                // 이전 activity를 모두 삭제하고 새로운 mainActivity 실행
                val mainActivity = Intent(this, MainActivity::class.java)
                mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(mainActivity)
            }else{
                Toast.makeText(AppClass.context, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // *** 구글로 로그인 시도
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                // 로그인이 성공되었을 때
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    AppClass.currentUser = auth.currentUser
                    // 구글 로그인한 사용자가 새로운 새로 가입한 경우
                    if (task.result.additionalUserInfo!!.isNewUser){
                        // 해당 유저 정보를 데이터베이스에 업로드 한다
                        FireDataUtil.addUserToDatabase(googleAccount.email!!, auth.uid!!)
                    }else{
                        FireDataUtil.getUerData()
                    }

                    // 이전 activity를 모두 삭제하고 새로운 mainActivity 실행
                    val mainActivity = Intent(this, MainActivity::class.java)
                    mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainActivity)
                }
                // 로그인이 실패했을 때
                else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

    // *** 다른 액티비티에서 돌아왔을 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Google 로그인 Activity에서 돌아왔을 때
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                googleAccount = account
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                // 입력한 로그인(google) 정보로 로그인을 시도한다
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
}