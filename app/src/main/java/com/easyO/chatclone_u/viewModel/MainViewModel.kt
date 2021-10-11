package com.easyO.chatclone_u.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    val fragmentIndex = MutableLiveData<Int>()
    init {
        fragmentIndex.value = 0
    }
}