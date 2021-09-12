package com.easyO.chatclone_u

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.easyO.chatclone_u.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {
    private lateinit var binder : ActivityMainBinding
    val mainFragment = MainFragment()
    val talkFragment = TalkFragment()
    val settingFragment = SettingFragment()

    val fragmentList = arrayOf(mainFragment, talkFragment, settingFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setActionBar(binder.toolbar)

        val adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        binder.viewPager2.adapter = adapter
    }
}