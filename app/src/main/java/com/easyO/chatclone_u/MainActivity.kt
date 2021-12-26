package com.easyO.chatclone_u

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.easyO.chatclone_u.chat.TalkFragment
import com.easyO.chatclone_u.databinding.ActivityMainBinding
import com.easyO.chatclone_u.home.MainFragment
import com.easyO.chatclone_u.setting.SettingFragment
import com.easyO.chatclone_u.viewModel.MainViewModel

class MainActivity : FragmentActivity() {
    private lateinit var binder : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    private val mainFragment = MainFragment()
    private val talkFragment = TalkFragment()
    private val settingFragment = SettingFragment()

    val fragmentList = arrayOf(mainFragment, talkFragment, settingFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // fragmentIndex 감시
        mainViewModel.fragmentIndex.observe(this, Observer {
            if (it == 0){
                binder.bottomMenu.setItemSelected(R.id.home)
            }else if (it == 1){
                binder.bottomMenu.setItemSelected(R.id.chat)
            }else if (it == 2){
                binder.bottomMenu.setItemSelected(R.id.settings)
            }
        })

        // Pager2 어댑터 정의
        val adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        // ViewPager2에 어댑터 적용
        binder.viewPager2.adapter = adapter
        // Pager를 이용하여 Fragment 전환을 할 때 리스너를 넣어서 어느 Fragment 보고 있는지 감시
        binder.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    // liveData를 변경하여 bottomBar의 선택 바꾸게 하기
                    mainViewModel.fragmentIndex.value = 0
                }
                else if (position == 1) {
                    mainViewModel.fragmentIndex.value = 1
                }
                else if (position == 2){
                    mainViewModel.fragmentIndex.value = 2
                }
                super.onPageSelected(position)
            }
        })

        // bottomBar 클릭에 따른 Pager2의 Fragment 변화시키기
        binder.bottomMenu.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    binder.viewPager2.currentItem = 0
                }
                R.id.chat -> {
                    binder.viewPager2.currentItem = 1
                }
                R.id.settings -> {
                    binder.viewPager2.currentItem = 2
                }
            }
        }
    }
}