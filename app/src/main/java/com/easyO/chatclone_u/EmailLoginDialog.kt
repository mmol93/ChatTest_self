package com.easyO.chatclone_u

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.easyO.chatclone_u.databinding.EmailLoginDialogBinding

class EmailLoginDialog(context: Context) : Dialog(context) {
    lateinit var binder : EmailLoginDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = EmailLoginDialogBinding.inflate(layoutInflater)

        setContentView(binder.root)

        // 배경을 투명하게 만들기
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}