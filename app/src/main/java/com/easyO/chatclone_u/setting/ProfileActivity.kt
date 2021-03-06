package com.easyO.chatclone_u.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.ActivityProfileBinding
import com.easyO.chatclone_u.util.FireDataUtil
import com.easyO.chatclone_u.util.FireStorage
import com.easyO.chatclone_u.util.MyConvertor
import com.easyO.chatclone_u.util.showToast

class ProfileActivity : AppCompatActivity() {
    private lateinit var binder : ActivityProfileBinding
    private var profileBitmap : Bitmap? = null
    val REQ_GALLERY = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        // 이미지 파일 클릭 시 갤러리 열어서 사진 업로드 하기
        // firebase에 업로드는 update 버튼을 눌렀을 때 한다
        binder.profileImageView.setOnClickListener {
            // 내부 파일을 가져오기 위해 필요한 권한
            val permission_list = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            var permissionChecker = 0

            // 권한 여부를 하나씩 확인 하기
            for(permission in permission_list){
                // 권한 체크
                val check = checkCallingOrSelfPermission(permission)
                if (check == PackageManager.PERMISSION_GRANTED){
                    permissionChecker = 1
                }else{
                    // 하나라도 허가 안된게 있을 때 -> 권한 요청
                    requestPermissions(permission_list, 0)
                    permissionChecker = 0
                }
            }
            // 모두 허가 되있을 때 -> 갤러리를 열어서 원하는 사진 선택
            if (permissionChecker == 1){
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivityForResult(intent, REQ_GALLERY)
            }
        }

        // 성별 스피너(드롭다운)에 대한 정의
        val sexList = arrayOf("Male", "Female", "None") // 스피너에 들어갈 항목
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        binder.sexSpinner.adapter = spinnerAdapter

        // 스피너는 override가 2개 이기때문에 listener에 대한 람다식이 없다
        // 그래서 이렇게 따로 정의를 해서 스피너 리스너에 등록해야함 필없...
//        val spinnerListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                when(parent?.id){
//                    R.id.sex_spinner->{
//
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        }
//
//        // 스피너에 리스너 등록 - 필없...
//        binder.sexSpinner.onItemSelectedListener = spinnerListener

        // *** update 버튼 클릭 시
        binder.buttonUpdate.setOnClickListener {
            // 모든 항목이 채워져있어야한다
            // 사진 데이터를 firebase Storage에 업로드
            // Users 폴더 -> uid 이름의 폴더 -> 여기에 profile이라는 이름으로 저장
            val firebasePath = "Users/" + AppClass.currentUser!!.uid + "/profile.jpg"
//            val bitmap = MyConvertor.getBitmapFromView(binder.profileImageView)
            // 업로드된 그림 파일은 view에 있는 그림이 아닌 원본을 올린다
            if (profileBitmap != null){
                FireStorage.firebaseUpload(firebasePath, profileBitmap!!, true)
            }

            // 텍스트 데이터를 database에 업로드
            val name = binder.nameEditText.text.toString()
            val sex = binder.sexSpinner.selectedItem.toString()
            val info = binder.selfEditText.text.toString()
            val age = binder.ageEditText.text.toString()
            Log.d("TAG", "name: $name")
            Log.d("TAG", "sex: $sex")
            Log.d("TAG", "info: $info")

            // 모든 항목이 채워져있어야한다
            if (name.length < 2){
                this.showToast("이름은 두 글자 이상입니다.")
                return@setOnClickListener
            }else if(info.length < 9){
                this.showToast("소개는 10글자 이상입니다.")
                return@setOnClickListener
            }else if(profileBitmap == null){
                this.showToast("프로필 사진은 반드시 등록해야합니다")
                return@setOnClickListener
            }else if(age.toInt() < 2){
                this.showToast("나이는 반드시 입력해야합니다.")
                return@setOnClickListener
            }

            // Text 데이터들을 firebase database에 업데이트
            FireDataUtil.userDataUpdate(name = name, sex = sex, info = info, age = age, uid = AppClass.currentUser!!.uid)
            finishAndRemoveTask()
        }

        // *** cancel 버튼 클릭 시
        binder.buttonCancel.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    // 갤러리에서 돌아 왔을 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_GALLERY && resultCode == RESULT_OK){
            if (data!!.data != null){
                val uriPicture = data!!.data

                // Uri에 있는 image 데이터를 bitmap으로 변환하기
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    profileBitmap = ImageDecoder
                        .decodeBitmap(ImageDecoder.createSource(getContentResolver(), uriPicture!!))
                }else{
                    profileBitmap = MediaStore
                        .Images.Media.getBitmap(getContentResolver(), uriPicture)
                }
                // 글라이드를 이용하여 imageView에 업로드
                Glide.with(application).load(uriPicture).into(binder.profileImageView)
            }else{
                profileBitmap = null
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (idx in grantResults.indices){
            if (grantResults[idx] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(AppClass.context, "Need external storage permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}