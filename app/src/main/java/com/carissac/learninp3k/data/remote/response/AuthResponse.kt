package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("msg")
    val msg: String? = null
)

data class LoginResponse (
    @field:SerializedName("msg")
    val msg: String? = null,
    @field:SerializedName("token")
    val token: String? = null,
    @field:SerializedName("login_result")
    val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("user_id")
    val userId: Int? = null,
    @field:SerializedName("user_name")
    val userName: String? = null,
    @field:SerializedName("user_email")
    val userEmail: String? = null
)