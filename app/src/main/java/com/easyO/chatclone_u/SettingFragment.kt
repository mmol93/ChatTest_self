package com.easyO.chatclone_u

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.easyO.chatclone_u.databinding.FragmentSettingBinding
import com.easyO.chatclone_u.viewModel.MainViewModel

class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var binder : FragmentSettingBinding
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_pref)
    }
}