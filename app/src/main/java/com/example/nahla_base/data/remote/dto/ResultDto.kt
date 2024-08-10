package com.example.nahla_base.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResultDto(
    @SerializedName("user_id") var userId: Int,
    @SerializedName("verification_code") var verificationCode: Int,
    @SerializedName("token") var token: String,
    @SerializedName("id") var id: Int,
    @SerializedName("is_success") var is_success: Any,
    @SerializedName("send_push") var pushStatus: Int,
    @SerializedName("is_verified") var is_verified: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("title") var title: String,
    @SerializedName("body") var body: String
)