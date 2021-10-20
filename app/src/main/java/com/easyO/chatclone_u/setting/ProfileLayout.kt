package com.easyO.chatclone_u.setting

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.MainActivity
import com.easyO.chatclone_u.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

// PreferenceScreen에 넣기 위해 layout을 Preference의 구성요소로 만들기
class ProfileLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        // 어느 layout Resource를 대상으로 할지 설정
        widgetLayoutResource = R.layout.profile_layout
    }

    // 해당 layout에 들어있는 view를 초기화 하고 기능 설정 가능
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        // 각 뷰에 데이터 넣기 from firebase
        // 프로필의 이미지는 firebase에서 가져오기 - before testing
//        val storage = Firebase.storage
//        val firebaseStorageRef = storage.reference
//        val firebaseImagesRef: StorageReference? = firebaseStorageRef.child("images").child("profiles")
//            .child(AppClass.currentUser!!.uid).child("profile.jpg")
//
//        // firebaseStore에서 프로필 사진 다운로드 하기
//        if (firebaseImagesRef != null){
//            val ONE_MEGABYTE: Long = 1024 * 1024
//            firebaseImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//                // byteArray를 bitmap으로 변환
//                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
//                holder.itemView.findViewById<CircleImageView>(R.id.profile_imageView).setImageBitmap(bitmap)
//            }.addOnFailureListener {
//                // Handle any errors
//                Toast.makeText(AppClass.context, "profile download failed", Toast.LENGTH_SHORT).show()
//            }
//        }
        holder.itemView.findViewById<TextView>(R.id.name_textView).text = "dd"


        // container layout에 있는 개별 view에 대해선 listener를 설정하는게 불가능하다
        // 대신 해당 container layout 전체에 대한 클릭 listener는 지정 가능
        holder.itemView.setOnClickListener {
            // 클릭 시 ProfileActivity 열기
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(context, intent, null)
        }
    }
}