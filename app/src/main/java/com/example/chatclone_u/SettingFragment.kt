package com.example.chatclone_u

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatclone_u.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binder : FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_setting, container, false)
        binder = FragmentSettingBinding.bind(layoutInflater)
        return layoutInflater
    }
}