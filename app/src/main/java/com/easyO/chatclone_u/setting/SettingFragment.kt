package com.easyO.chatclone_u.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R

class SettingFragment : PreferenceFragmentCompat() {
    lateinit var pref : SharedPreferences
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_pref)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.fragment_background)
        pref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == "HashList") {
            // user clicked the item
            val hashSwitch = pref.getBoolean("HashList", false)
            val tag1TextView = findPreference<EditTextPreference>("Hash1")
            val tag2TextView = findPreference<EditTextPreference>("Hash2")
            val tag3TextView = findPreference<EditTextPreference>("Hash3")
            val tag4TextView = findPreference<EditTextPreference>("Hash4")
            val tag5TextView = findPreference<EditTextPreference>("Hash5")

            if (hashSwitch){
                tag1TextView!!.isVisible = true
                tag2TextView!!.isVisible = true
                tag3TextView!!.isVisible = true
                tag4TextView!!.isVisible = true
                tag5TextView!!.isVisible = true
            }else{
                tag1TextView!!.isVisible = false
                tag2TextView!!.isVisible = false
                tag3TextView!!.isVisible = false
                tag4TextView!!.isVisible = false
                tag5TextView!!.isVisible = false
            }
            return true
        }else if (preference.key == "About"){
            Toast.makeText(AppClass.context, "About Clicked", Toast.LENGTH_SHORT).show()
            return true
        }else if (preference.key == "Logout"){
            Toast.makeText(AppClass.context, "Logout Clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    // (주로)ProfileActivity에서 돌아왔을 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}