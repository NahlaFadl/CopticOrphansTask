package com.example.nahla_base.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.nahla_base.utils.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preferencesModule  = module {
    single { provideSettingsPreferences(androidApplication()) }
}

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(Constants.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)