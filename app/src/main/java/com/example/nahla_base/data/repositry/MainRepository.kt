package com.example.firebaseapp.data.repositry

import com.example.firebaseapp.data.remote.RetrofitApi
import com.example.firebaseapp.data.remote.networkHandling.NetworkResult.getResult

class MainRepository(var apiService: RetrofitApi) {

    suspend fun getRepositories(page:Int) = getResult{apiService.getRepositories(page)}
    suspend fun getSearch(txtSearch:String,page:Int) = getResult{apiService.getSearch(txtSearch,page)}
}