package com.easyO.chatclone_u.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyO.chatclone_u.model.User

class MainViewModel : ViewModel(){
    val fragmentIndex = MutableLiveData<Int>()
    init {
        fragmentIndex.value = 0
    }
}