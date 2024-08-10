package com.example.nahla_base.ui

import com.example.nahla_base.base.BaseViewModel
import com.example.nahla_base.data.repositry.MainRepository
import com.example.nahla_base.utils.AppManger

class MainViewModel(
    override val appManger: AppManger,
    private val mainRepository: MainRepository,
) : BaseViewModel(appManger) {


}