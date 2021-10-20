package com.easyO.chatclone_u.util

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
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
    fun firebaseUpload(firebasePath:String, imageData:Bitmap){
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
        }
    }
}