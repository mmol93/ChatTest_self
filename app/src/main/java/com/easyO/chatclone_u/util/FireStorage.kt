package com.easyO.chatclone_u.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.easyO.chatclone_u.AppClass
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

object FireStorage {
    // firebase Storage에서 파일 다운로드
    fun firebaseDownLocal(firebasePath:String, saveName:String, fileExtension:String){
        val storage = Firebase.storage
        val firebaseStorageRef = storage.reference
        val pathReference = firebaseStorageRef.child(firebasePath)
        val localFile = File.createTempFile(saveName, fileExtension)

        pathReference.getFile(localFile).addOnSuccessListener {
            Log.d("TAG", "file path: ${localFile.absolutePath}")
            Toast.makeText(AppClass.context, "download success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(AppClass.context, "download fail", Toast.LENGTH_SHORT).show()
        }
    }

    // firebase Storage에 파일 업로드
    fun firebaseUpload(firebasePath:String, imageData:Bitmap, trigger:Boolean){
        val storage = Firebase.storage
        val firebaseStorageRef = storage.reference
        val imageRef = firebaseStorageRef.child(firebasePath)
        val baos = ByteArrayOutputStream()
        imageData.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("TAG", "imageUpload Failed")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Log.d("TAG", "imageUpload Success")
            if (trigger){
                // firebase의 데이터베이스는 같은 값을 넣었을 경우 리스너의 onChange()를 호출하지 않는다
                // 그렇기 때문에 preference를 이용하여 내부에서 데이터를 읽어서 서로 다른 데이터를 보내준다
                val pref = PreferenceManager.getDefaultSharedPreferences(AppClass.context)
                val prefTrigger = pref.getString("trigger", "1")
                val editor = pref.edit()

                // 프로필 사진 갱신을 위해 firebase 데이터베이스에 값을 하나 넣어준다
                if (prefTrigger == "0"){
                    FireDataUtil.triggerDataUpload(AppClass.currentUser!!.uid, "1")
                    editor.putString("trigger", "1")
                    editor.apply()
                }else{
                    FireDataUtil.triggerDataUpload(AppClass.currentUser!!.uid, "0")
                    editor.putString("trigger", "0")
                    editor.apply()
                }

                val prefTrigger2 = pref.getString("trigger", "0")
                Log.d("Firebase", "listen: $prefTrigger2")
            }
        }
    }
}