package com.carissac.learninp3k.data.remote

import com.carissac.learninp3k.data.remote.response.AvatarDetailResponse
import com.carissac.learninp3k.data.remote.response.AvatarResponseItem
import com.carissac.learninp3k.data.remote.response.CourseEnrollmentResponse
import com.carissac.learninp3k.data.remote.response.CourseResponse
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.NewsResponseItem
import com.carissac.learninp3k.data.remote.response.ProfileResponse
import com.carissac.learninp3k.data.remote.response.ProfileResultResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.remote.response.SearchNewsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("user_email")
        email: String,
        @Field("user_password")
        password: String
    ): LoginResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

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
    ): ProfileResultResponse

    @GET("avatars")
    suspend fun getAllAvatar(
        @Header("Authorization") token: String
    ): List<AvatarResponseItem>

    @GET("avatars/{id}")
    suspend fun getAvatarDetail(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): AvatarDetailResponse

    @GET("news/date")
    suspend fun getAllNews(
        @Header("Authorization") token: String,
        @Query("sort")
        sort: String = "desc"
    ): List<NewsResponseItem>

    @GET("news/{id}")
    suspend fun getNewsDetail(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): NewsResponseItem

    @GET("news/date")
    suspend fun searchNews(
        @Header("Authorization") token: String,
        @Query("news_title")
        newsTitle: String
    ): SearchNewsResponse

    @FormUrlEncoded
    @POST("courses/enroll")
    suspend fun enrollCourse(
        @Header("Authorization") token: String,
        @Field("course_id")
        courseId: Int
    ): CourseEnrollmentResponse

    @GET("courses")
    suspend fun getAllCourse(
        @Header("Authorization") token: String,
    ): CourseResponse

    // TODO: https://github.com/Ca-ri-ssa/TA-LearninP3K-BackEnd/blob/main/api-docs.md#get-all-course-get
}