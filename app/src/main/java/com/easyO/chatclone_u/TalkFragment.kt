package com.easyO.chatclone_u

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.easyO.chatclone_u.databinding.FragmentTalkBinding

class TalkFragment : Fragment() {
    private lateinit var binder : FragmentTalkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_talk, container, false)
        binder = FragmentTalkBinding.bind(layoutInflater)
        return layoutInflater
    }
}