package com.example.firebaseapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import java.util.Locale

class AppManger constructor(var sharedPreferences: SharedPreferences) {

    companion object {
        const val LANGUAGE = "lang"
        const val TOKEN = "token"
        const val FCM_TOKEN = "fcmToken"
        var language = "ar"
    }


    fun getLanguage(): String {
        language = sharedPreferences.getString(LANGUAGE, "ar") ?: "ar"
        return language
    }

    fun getUserToken() = sharedPreferences.getString(TOKEN, "") ?: ""

    fun getFcmToken() = sharedPreferences.getString(FCM_TOKEN, "Not Allowed") ?: "Not Allowed"


}