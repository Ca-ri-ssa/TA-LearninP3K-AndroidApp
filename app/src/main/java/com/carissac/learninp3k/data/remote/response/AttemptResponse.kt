package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class AttemptResponse (
    @field:SerializedName("attempt_session_id")
    val attemptSessionId: Int? = null,

    @field:SerializedName("attempt_score")
    val attemptScore: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("attempts")
    val attempts: List<AttemptDetailResponse>? = null
)

data class AttemptDetailResponse (
    @field:SerializedName("attempt_id")
    val attemptId: Int? = null,

    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("question_content")
    val questionContent: String? = null,

    @field:SerializedName("option_1")
    val option1: String? = null,

    @field:SerializedName("option_2")
    val option2: String? = null,

    @field:SerializedName("option_3")
    val option3: String? = null,

    @field:SerializedName("option_4")
    val option4: String? = null,

    @field:SerializedName("user_answer")
    val userAnswer: String? = null,

    @field:SerializedName("correct_answer")
    val correctAnswer: String? = null,

    @field:SerializedName("is_correct")
    val isCorrect: Boolean? = null,

    @field:SerializedName("feedback")
    val feedback: String? = null
)

data class AllAttemptResponse (
    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("attempts")
    val attempts: List<AllAttemptResponseItem>? = null
)

data class AllAttemptResponseItem (
    @field:SerializedName("attempt_session_number")
    val attemptSessionNum: String? = null,

    @field:SerializedName("attempt_session_id")
    val attemptSessionId: Int? = null,

    @field:SerializedName("attempt_score")
    val attemptScore: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null
)