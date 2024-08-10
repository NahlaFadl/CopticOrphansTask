package com.example.nahla_base.di.modules

import com.example.nahla_base.data.repositry.MainRepository
import com.example.nahla_base.utils.AppManger
import org.koin.dsl.module

val classesModule = module {
    single { AppManger(get()) }
    single { MainRepository(get()) }
}