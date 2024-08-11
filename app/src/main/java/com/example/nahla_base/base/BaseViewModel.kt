package com.example.firebaseapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebaseapp.utils.AppManger

open class BaseViewModel(open val appManger: AppManger) : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val showMassage = MutableLiveData<String>()
}