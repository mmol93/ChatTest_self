package com.easyO.chatclone_u.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.easyO.chatclone_u.AppClass
import com.easyO.chatclone_u.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_pref)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.fragment_background)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == "HashList") {
            // user clicked the item
            Toast.makeText(AppClass.context, "Hash Clicked", Toast.LENGTH_SHORT).show()
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
}