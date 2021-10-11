package com.easyO.chatclone_u

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.easyO.chatclone_u.databinding.ActivityMainBinding
import com.easyO.chatclone_u.viewModel.MainViewModel

class MainActivity : FragmentActivity() {
    private lateinit var binder : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    val mainFragment = MainFragment()
    val talkFragment = TalkFragment()
    val settingFragment = SettingFragment()

    val fragmentList = arrayOf(mainFragment, talkFragment, settingFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.fragmentIndex.observe(this, Observer {
            if (it == 0){
                binder.bottomMenu.setItemSelected(R.id.home)
            }else if (it ==1){
                binder.bottomMenu.setItemSelected(R.id.activity)
            }else if (it ==2){
                binder.bottomMenu.setItemSelected(R.id.favorites)
            }
            Log.d("TAG", "fragmentItem in liveData = $it")
        })

        val adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        binder.viewPager2.adapter = adapter
        binder.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    mainViewModel.fragmentIndex.value = 0
                }
                else if (position == 1) {
                    mainViewModel.fragmentIndex.value = 1
                }
                else if (position == 2){
                    mainViewModel.fragmentIndex.value = 2
                }
                Log.d("TAG", "fragment index: $position")
                super.onPageSelected(position)
            }
        })
    }
}