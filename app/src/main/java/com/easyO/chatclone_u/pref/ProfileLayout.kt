package com.easyO.chatclone_u.pref

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
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
        // 어느 layout Resource를 대상으로 할지 설정
        widgetLayoutResource = R.layout.profile_layout
    }

    // 해당 layout에 들어있는 view를 초기화 하고 기능 설정 가능
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        // container layout에 있는 개별 view에 대해선 listener를 설정하는게 불가능하다
        // 대신 해당 container layout 전체에 대한 클릭 listener는 지정 가능
        holder.itemView.setOnClickListener {
            Toast.makeText(AppClass.context, "dd", Toast.LENGTH_SHORT).show()
        }

        // 각각의 뷰에 대한 정의는 가능
        holder.itemView.findViewById<TextView>(R.id.name_textView).text = "dd"
    }
}