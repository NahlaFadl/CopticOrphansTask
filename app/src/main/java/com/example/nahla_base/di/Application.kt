package com.example.nahla_base.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.example.nahla_base.di.modules.classesModule
import com.example.nahla_base.di.modules.networkModule
import com.example.nahla_base.di.modules.preferencesModule
import com.example.nahla_base.di.modules.viewModelModule
import com.example.nahla_base.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Application)
            modules(networkModule, preferencesModule, viewModelModule, classesModule)
            createNotificationChannels()
        }
    }

    private fun createNotificationChannels() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultChannel = NotificationChannel(
                Constants.DEFAULT_CHANNEL_ID,
                Constants.DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description="created for default"
                lightColor = Color.GREEN
                enableLights(true)
                setShowBadge(true)
            }

            notificationManager.createNotificationChannel(defaultChannel)
        }
    }

}