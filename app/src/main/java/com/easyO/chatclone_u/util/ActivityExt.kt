package com.easyO.chatclone_u.util

import android.app.Activity
import android.widget.Toast
import com.easyO.chatclone_u.AppClass

fun Activity.showToast(msg:String){
    Toast.makeText(AppClass.context, msg, Toast.LENGTH_SHORT).show()
}