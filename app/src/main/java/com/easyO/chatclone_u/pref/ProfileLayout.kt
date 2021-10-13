package com.easyO.chatclone_u.pref

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R

// PreferenceScreen에 넣기 위해 layout을 Preference의 구성요소로 만들기
class ProfileLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        // 어느 layoutResource를 대상으로 할지 설정
        widgetLayoutResource = R.layout.profile_layout
    }

    // 해당 layout에 들어있는 view를 초기화 하고 기능 설정 가능
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        Toast.makeText(AppClass.context, "test", Toast.LENGTH_SHORT).show()
        //holder.itemView.findViewById<TextView>(R.id.name_textView)
    }
}