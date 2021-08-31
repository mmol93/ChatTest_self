package com.example.chatclone_u

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatclone_u.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binder : FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_main, container, false)
        binder = FragmentMainBinding.bind(layoutInflater)
        // Inflate the layout for this fragment
        return layoutInflater
    }
}