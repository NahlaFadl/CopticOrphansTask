package com.example.firebaseapp.data.remote.networkHandling

import retrofit2.Response

interface NetworkStatus {
    fun onBadRequest(exception: String?)
    fun onMakeAction(exception: String?)
    fun onNotVerifyRequest(exception: String?)
    fun onNotAuthorized(exception: String?)
    fun onServerSideError(exception: String?)
    fun <T> onDynamicCode(response: Response<T>)
    fun onConnectionFail(exception: String?)
    fun onTooManyRequests(exception: String?)
    fun onNotAllowed()
    fun onApiNotFound()
    fun onNoInternet()
}