package com.example.firebaseapp.di.modules

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import com.example.firebaseapp.data.remote.RetrofitApi
import com.example.firebaseapp.utils.AppManger
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { getLoggingInterceptor() }
    single { getOkHttp(get(), get(), get()) }
    single { getRetrofit(get()) }
    single { getRetrofitApi(get()) }
}

fun getLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    return httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}

fun getOkHttp(
    context: Context,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    appManger: AppManger
): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
                .addHeader("Accept-Language", appManger.getLanguage())
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-Type", "android")
                .addHeader("Device-Name", android.os.Build.MODEL)
                .addHeader("Device-OS-Version", android.os.Build.VERSION.RELEASE)
                .addHeader(
                    "Device-UDID",
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                )
                .addHeader("Device-Push-Token", appManger.getFcmToken())
                .addHeader("mobile_version", android.os.Build.VERSION.SDK_INT.toString())
//                .addHeader("Authorization", "Bearer ${appManger.getUserToken()}")

            try {
                val version =
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                builder.addHeader("App-Version", version)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                builder.addHeader("App-Version", "1")
            }

            chain.proceed(builder.build())
        }.build()
}


fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gson = GsonBuilder()
        .registerTypeAdapter(HttpUrl::class.java, UrlDeserializer())
        .create()
    return Retrofit.Builder()
        .baseUrl(RetrofitApi.getBaseUrl())
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun getRetrofitApi(retrofit: Retrofit): RetrofitApi {
    return retrofit.create(RetrofitApi::class.java)
}

class UrlDeserializer : JsonDeserializer<HttpUrl> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): HttpUrl =
        json.asString.toHttpUrl()

}