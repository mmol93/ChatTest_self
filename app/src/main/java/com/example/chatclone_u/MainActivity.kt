package com.example.chatclone_u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatclone_u.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {
    private lateinit var binder : ActivityMainBinding
    val mainFragment = MainFragment()
    val talkFragment = TalkFragment()
    val settingFragment = SettingFragment()

    val fragmentList = arrayOf(mainFragment, talkFragment, settingFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = ActivityMainBinding.inflate(layoutInflater)

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
        setContentView(binder.root)
    }
}