package com.example.firebaseapp.data.remote.networkHandling

import android.util.Log
import retrofit2.Response

object NetworkResult {

    private lateinit var status: NetworkStatus

    fun observeNetworkStatus(status: NetworkStatus) {
        NetworkResult.status = status
    }

    suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            } else {
                when (response.code()) {
                    500 -> status.onServerSideError(response.errorBody()?.source()?.buffer?.readUtf8())
                    406 -> status.onNotVerifyRequest(response.errorBody()?.source()?.buffer?.readUtf8())
                    405 -> status.onNotAllowed()
                    404 -> status.onApiNotFound()
                    401 -> status.onNotAuthorized(response.errorBody()?.source()?.buffer?.readUtf8())
                    400 -> status.onMakeAction(response.errorBody()?.source()?.buffer?.readUtf8())
                    422 -> status.onBadRequest(response.errorBody()?.source()?.buffer?.readUtf8())
                    429 -> status.onTooManyRequests(response.errorBody()?.source()?.buffer?.readUtf8())
                    else -> status.onDynamicCode(response)
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message!!)
            status.onConnectionFail(e.message.toString())
        }
        return Resource.error("")
    }

}