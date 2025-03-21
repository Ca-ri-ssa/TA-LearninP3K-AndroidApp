package com.carissac.learninp3k.data.remote.retrofit

import com.carissac.learninp3k.data.remote.response.AllAttemptResponse
import com.carissac.learninp3k.data.remote.response.AllAttemptResponseItem
import com.carissac.learninp3k.data.remote.response.AttemptResponse
import com.carissac.learninp3k.data.remote.response.AvatarDetailResponse
import com.carissac.learninp3k.data.remote.response.AvatarResponseItem
import com.carissac.learninp3k.data.remote.response.CourseDetailResponse
import com.carissac.learninp3k.data.remote.response.CourseEnrollmentResponse
import com.carissac.learninp3k.data.remote.response.CourseIntroResponse
import com.carissac.learninp3k.data.remote.response.CourseNearestResponse
import com.carissac.learninp3k.data.remote.response.CourseResponse
import com.carissac.learninp3k.data.remote.response.CourseStatusResponse
import com.carissac.learninp3k.data.remote.response.DetailNewsResponse
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.data.remote.response.LoginResponse
import com.carissac.learninp3k.data.remote.response.NewsResponseItem
import com.carissac.learninp3k.data.remote.response.ProfileResponse
import com.carissac.learninp3k.data.remote.response.ProfileResultResponse
import com.carissac.learninp3k.data.remote.response.QuizResponse
import com.carissac.learninp3k.data.remote.response.RegisterResponse
import com.carissac.learninp3k.data.remote.response.SearchNewsResponse
import com.carissac.learninp3k.data.remote.response.SubmitAttemptRequest
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.data.remote.response.TakeWeeklyChallengeResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponse
import com.carissac.learninp3k.data.remote.response.UserBadgeResponseItem
import com.carissac.learninp3k.data.remote.response.UserLeaderboardResponse
import com.carissac.learninp3k.data.remote.response.WeeklyChallengeResponse
import retrofit2.Response
import retrofit2.http.Body
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
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("user_email")
        email: String,
        @Field("user_password")
        password: String
    ): Response<LoginResponse>

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @FormUrlEncoded
    @PUT("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("user_name")
        name: String,
        @Field("user_email")
        email: String,
        @Field("avatar_id")
        avatarId: Int?
    ): Response<ProfileResultResponse>

    @GET("avatars")
    suspend fun getAllAvatar(
        @Header("Authorization") token: String
    ): Response<List<AvatarResponseItem>>

    @GET("avatars/{id}")
    suspend fun getAvatarDetail(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<AvatarDetailResponse>

    @GET("news/date")
    suspend fun getAllNews(
        @Header("Authorization") token: String,
        @Query("sort")
        sort: String = "desc"
    ): Response<List<NewsResponseItem>>

    @GET("news/{id}")
    suspend fun getNewsDetail(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<DetailNewsResponse>

    @GET("news/search")
    suspend fun searchNews(
        @Header("Authorization") token: String,
        @Query("news_title")
        newsTitle: String
    ): Response<SearchNewsResponse>

    @FormUrlEncoded
    @POST("courses/enroll")
    suspend fun enrollCourse(
        @Header("Authorization") token: String,
        @Field("course_id")
        courseId: Int
    ): Response<CourseEnrollmentResponse>

    @GET("courses")
    suspend fun getAllCourse(
        @Header("Authorization") token: String,
    ): Response<CourseResponse>

    @GET("courses/{id}/intro")
    suspend fun getIntroCourse(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<CourseIntroResponse>

    @GET("courses/{id}/detail")
    suspend fun getCourseDetail(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<CourseDetailResponse>

    @GET("courses/status")
    suspend fun getCourseByStatus(
        @Header("Authorization") token: String,
        @Query("status")
        status: String
    ): Response<CourseStatusResponse>

    @GET("courses/{id}/quiz")
    suspend fun getCourseQuiz(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<List<QuizResponse>>

    @GET("courses/nearest")
    suspend fun getNearestCourse(
        @Header("Authorization") token: String
    ): Response<CourseNearestResponse>


    @POST("courses/{id}/quiz/submit")
    suspend fun takeAttempt(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int,
        @Body request: SubmitAttemptRequest
    ): Response<TakeAttemptResponse>

    @GET("courses/{id}/quiz/attempts")
    suspend fun getAllAttemptSession(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<List<AllAttemptResponseItem>>

    @GET("courses/{id}/quiz/attempts/{sessionId}")
    suspend fun getDetailAttempt(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int,
        @Path("sessionId")
        sessionId: Int
    ): Response<AttemptResponse>

    @GET("challenges")
    suspend fun getWeeklyChallenge(
        @Header("Authorization") token: String
    ): Response<WeeklyChallengeResponse>

    @FormUrlEncoded
    @POST("challenges/submit")
    suspend fun takeWeeklyChallenge(
        @Header("Authorization") token: String,
        @Field("challenge_id")
        challengeId: Int,
        @Field("user_answer")
        userAnswer: String
    ): Response<TakeWeeklyChallengeResponse>

    @GET("leaderboards")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String
    ): Response<List<LeaderboardResponse>>

    @GET("leaderboards/users")
    suspend fun getUserLeaderboard(
        @Header("Authorization") token: String
    ): Response<UserLeaderboardResponse>

    @GET("leaderboards/users/badges")
    suspend fun getAllUserBadge(
        @Header("Authorization") token: String,
        @Query("category")
        category: String = "all"
    ): Response<List<UserBadgeResponseItem>>

    @GET("leaderboards/users/badges/{id}")
    suspend fun getUserBadgeById(
        @Header("Authorization") token: String,
        @Path("id")
        id: Int
    ): Response<UserBadgeResponse>
}