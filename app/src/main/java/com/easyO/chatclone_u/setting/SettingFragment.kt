package com.easyO.chatclone_u.setting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.preference.*
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.LoginActivity
import com.easyO.chatclone_u.R
import com.easyO.chatclone_u.model.User
import com.easyO.chatclone_u.util.FireDataUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SettingFragment : PreferenceFragmentCompat() {
    lateinit var prefManager : SharedPreferences
    lateinit var tag1TextView : EditTextPreference
    lateinit var tag2TextView : EditTextPreference
    lateinit var tag3TextView : EditTextPreference
    lateinit var tag4TextView : EditTextPreference
    lateinit var tag5TextView : EditTextPreference
    lateinit var logout: Preference
    var rootKey_forRenew : String? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_pref, rootKey)
        rootKey_forRenew = rootKey
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.fragment_background)
        prefManager = PreferenceManager.getDefaultSharedPreferences(context)

        // SettingFragment UI 초기화
        tag1TextView = findPreference<EditTextPreference>("Hash1")!!
        tag2TextView = findPreference<EditTextPreference>("Hash2")!!
        tag3TextView = findPreference<EditTextPreference>("Hash3")!!
        tag4TextView = findPreference<EditTextPreference>("Hash4")!!
        tag5TextView = findPreference<EditTextPreference>("Hash5")!!

        hashSwitch()

        // 이 리스너는 최초 호출 시 반드시 1번 호출됨
        var once = 0
        val userRef = FirebaseDatabase.getInstance().reference.child("user")
            .child(AppClass.currentUser!!.uid).child("trigger")
        val nameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    // Storage의 경우 listener가 없기 때문에 여기서 profile 이미지를 갱신한다
                    // 최초 발동 시에는 발동하지 않게 한다
                    if (once == 1){
                        // setPreferencesFromResource를 하면 기존에 tag1TextView가 아얘 바뀌어버린다
                        // 그래서 setPreferencesFromResource를 한 이후 다시 이 부분을 재정의 하지 않으면
                        // 제대로 기능을 하지 않음
                        setPreferencesFromResource(R.xml.setting_pref, rootKey_forRenew)

                        tag1TextView = findPreference<EditTextPreference>("Hash1")!!
                        tag2TextView = findPreference<EditTextPreference>("Hash2")!!
                        tag3TextView = findPreference<EditTextPreference>("Hash3")!!
                        tag4TextView = findPreference<EditTextPreference>("Hash4")!!
                        tag5TextView = findPreference<EditTextPreference>("Hash5")!!

                        hashSwitch()
                    }else{
                        once = 1
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(AppClass.context, "Please set again", Toast.LENGTH_SHORT).show()
            }
        }
        userRef.addValueEventListener(nameListener)

        // 각 hashTag의 텍스트뷰의 값이 변경되면 데이터를 업로드한다. - 리스너 붙임
        tag1TextView.setOnPreferenceChangeListener { preference, newValue ->
            FireDataUtil.userTagUpdate(uid = AppClass.currentUser!!.uid, newValue.toString(), null, null, null, null)
            return@setOnPreferenceChangeListener true
        }
        tag2TextView.setOnPreferenceChangeListener { preference, newValue ->
            FireDataUtil.userTagUpdate(uid = AppClass.currentUser!!.uid, null, newValue.toString(), null, null, null)
            return@setOnPreferenceChangeListener true
        }
        tag3TextView.setOnPreferenceChangeListener { preference, newValue ->
            FireDataUtil.userTagUpdate(uid = AppClass.currentUser!!.uid, null, null, newValue.toString(), null, null)
            return@setOnPreferenceChangeListener true
        }
        tag4TextView.setOnPreferenceChangeListener { preference, newValue ->
            FireDataUtil.userTagUpdate(uid = AppClass.currentUser!!.uid, null, null, null, newValue.toString(), null)
            return@setOnPreferenceChangeListener true
        }
        tag5TextView.setOnPreferenceChangeListener { preference, newValue ->
            FireDataUtil.userTagUpdate(uid = AppClass.currentUser!!.uid, null, null, null, null, newValue.toString())
            return@setOnPreferenceChangeListener true
        }
    }

    // 각 Preference 버튼을 클락했을 때 동작 지정
    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == "HashList") {
            hashSwitch()
            return true
        }else if (preference.key == "About"){
            Toast.makeText(AppClass.context, "About Clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        // 로그아웃 처리
        else if (preference.key == "Logout"){
            // 로그아웃 확인을 위한 팝업 = Dialog
            val dialogBuilder = AlertDialog.Builder(context)

            val positiveListener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    // AppClass에 있는 유저 정보도 초기화한다
                    AppClass.hasUserInfo = false
                    AppClass.currentUser = null

                    // 로그 아웃 및 MainActivity 종료
                    activity?.finishAndRemoveTask()
                    Firebase.auth.signOut()

                    // 로그인 창 새로 열기
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    activity?.startActivity(loginIntent)
                }
            }
            dialogBuilder.setTitle("ChatClone")
            dialogBuilder.setMessage("Really want logout?")
            dialogBuilder.setPositiveButton("YES", positiveListener)
            dialogBuilder.setNeutralButton("No", null)
            dialogBuilder.show()
            return true
        }
        return false
    }

    fun hashSwitch(){
        // user clicked the item
        val hashSwitch = prefManager.getBoolean("HashList", false)

        Toast.makeText(AppClass.context, "hashSwitch $hashSwitch", Toast.LENGTH_SHORT).show()

        if (hashSwitch){
            tag1TextView.isVisible = true
            tag2TextView.isVisible = true
            tag3TextView.isVisible = true
            tag4TextView.isVisible = true
            tag5TextView.isVisible = true
        }else{
            tag1TextView.isVisible = false
            tag2TextView.isVisible = false
            tag3TextView.isVisible = false
            tag4TextView.isVisible = false
            tag5TextView.isVisible = false
        }
    }
}