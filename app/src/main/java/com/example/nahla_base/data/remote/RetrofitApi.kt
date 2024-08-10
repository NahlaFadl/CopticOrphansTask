package com.example.nahla_base.data.remote

import com.example.nahla_base.data.remote.dto.RepositoriesData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    companion object{
        private const val BASE_URL_LIVE = "https://api.github.com/"
        fun getBaseUrl()= BASE_URL_LIVE
    }

    @GET("repositories")
    suspend fun getRepositories(
        @Query("page") page:Int
    ):Response<List<RepositoriesData>>
}