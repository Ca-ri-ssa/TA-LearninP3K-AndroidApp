package com.carissac.learninp3k.data.remote

import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.ProfileResponse
import com.carissac.learninp3k.data.remote.response.ProfileResultResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("user_name")
        name: String,
        @Field("user_email")
        email: String,
        @Field("user_password")
        password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("user_email")
        email: String,
        @Field("user_password")
        password: String
    ): Call<LoginResponse>

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PUT("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("user_name")
        name: String,
        @Field("user_email")
        email: String,
        @Field("avatar_id")
        avatarId: Int
    ): Call<ProfileResultResponse>
}