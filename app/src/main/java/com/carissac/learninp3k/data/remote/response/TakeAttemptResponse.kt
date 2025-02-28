package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class Answer (
    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("user_answer")
    val userAnswer: String? = null,
)

data class SubmitAttemptRequest (
    @field:SerializedName("answers")
    val answers: List<Answer>? = null
)

data class TakeAttemptResponse (
    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("points_earned")
    val pointsEarned: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("attempt_session_id")
    val attemptSessionId: Int?= null,

    @field:SerializedName("badge_name")
    val badgeName: String? = null,

    @field:SerializedName("badge_img")
    val badgeImg: String? = null
)