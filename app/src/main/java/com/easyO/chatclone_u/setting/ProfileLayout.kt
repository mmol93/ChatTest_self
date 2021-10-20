package com.easyO.chatclone_u.setting

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        // 프로필 이미지 로딩은 이 클래스 내부에서 2번 사용되기 때문에 fun으로 묶어서 쓴다
        // 첫 번째: 처음 화면 생성 시 로딩
        // 두 번째: 사진 변경 시 다시 로딩
        fun reloadProfilePicture(){
            // 각 뷰에 데이터 넣기 from firebase
            // 프로필의 이미지는 firebase에서 가져오기 - before testing
            val storage = Firebase.storage
            val firebaseStorageRef = storage.reference
            val firebaseImagesRef: StorageReference? = firebaseStorageRef.child("Users")
                .child(AppClass.currentUser!!.uid).child("profile.jpg")

            // firebaseStore에서 프로필 사진 다운로드 하기
            if (firebaseImagesRef != null){
                val ONE_MEGABYTE: Long = 1024 * 1024
                firebaseImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    // byteArray를 bitmap으로 변환
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    holder.itemView.findViewById<CircleImageView>(R.id.profile_imageView).setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                    Toast.makeText(AppClass.context, "profile download failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // firebase에서 프로필 정보(텍스트) 가져오기
        // 1. 각종 Ref 정의
        val userRef = FirebaseDatabase.getInstance().reference.child("user")
            .child(AppClass.currentUser!!.uid)

        val nameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val user = dataSnapshot.getValue(User::class.java)
                    // 이름
                    holder.itemView.findViewById<TextView>(R.id.name_textView).text = user!!.name
                    // 소개
                    holder.itemView.findViewById<TextView>(R.id.selfInfo_textView).text = user.info
                    // Storage의 경우 listern가 없기 때문에 여기서 profile 이미지를 갱신한다
                    reloadProfilePicture()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(AppClass.context, "Please set again", Toast.LENGTH_SHORT).show()
            }
        }
        userRef.addValueEventListener(nameListener)

        // container layout에 있는 개별 view에 대해선 listener를 설정하는게 불가능하다
        // 대신 해당 container layout 전체에 대한 클릭 listener는 지정 가능
        holder.itemView.setOnClickListener {
            // 클릭 시 ProfileActivity 열기
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(context, intent, null)
        }
    }
}