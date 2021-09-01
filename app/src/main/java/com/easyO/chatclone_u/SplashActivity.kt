package com.easyO.chatclone_u

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

//        finishAffinity()
//        startActivity(loginIntent)

        val handler = object : Handler(Looper.myLooper()!!){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                finishAffinity()
                startActivity(loginIntent)
            }
        }

        thread {
            SystemClock.sleep(1600)
            val msg = Message()
            handler.sendMessage(msg)
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}