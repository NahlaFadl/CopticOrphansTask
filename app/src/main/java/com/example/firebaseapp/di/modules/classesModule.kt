package com.example.firebaseapp.di.modules

import com.example.firebaseapp.data.repositry.MainRepository
import com.example.firebaseapp.utils.AppManger
import org.koin.dsl.module

val classesModule = module {
    single { AppManger(get()) }
    single { MainRepository(get()) }
}