package com.easyO.chatclone_u.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binder : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        // 이미지 파일 클릭 시 갤러리 열어서 사진 선택하게 하기
        binder.profileImageView.setOnClickListener {

        }

        // 성별 스피너(드롭다운)에 대한 정의
        val sexList = arrayOf("Male", "Female", "None") // 스피너에 들어갈 항목
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        binder.sexSpinner.adapter = spinnerAdapter

        // 스피너는 override가 2개 이기때문에 listener에 대한 람다식이 없다
        // 그래서 이렇게 따로 정의를 해서 스피너 리스너에 등록해야함
        val spinnerListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(parent?.id){
                    R.id.sex_spinner->{

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        // 스피너에 리스너 등록
        binder.sexSpinner.onItemSelectedListener = spinnerListener

        // nameEditText에 입력을 완료했을 때

    }
}