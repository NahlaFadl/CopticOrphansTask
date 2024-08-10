package com.example.nahla_base.data.repositry

import com.example.nahla_base.data.remote.RetrofitApi
import com.example.nahla_base.data.remote.networkHandling.NetworkResult.getResult

class MainRepository(var apiService: RetrofitApi) {

    suspend fun getRepositories(page:Int) = getResult{apiService.getRepositories(page)}
}