package com.easyO.chatclone_u.util

import android.content.Context
import android.widget.Toast
import com.easyO.chatclone_u.AppClass


fun Context.showToast(msg:String){
    Toast.makeText(AppClass.context, msg, Toast.LENGTH_SHORT).show()
}
