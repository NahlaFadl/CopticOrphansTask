package com.example.nahla_base.data.remote

interface RetrofitApi {
    companion object{
        private const val BASE_URL_LIVE = "http://live.com"
        fun getBaseUrl()= BASE_URL_LIVE
    }
}