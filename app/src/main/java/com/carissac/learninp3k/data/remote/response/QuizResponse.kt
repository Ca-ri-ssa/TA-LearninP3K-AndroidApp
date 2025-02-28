package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @field:SerializedName("question_id")
    val questionId: Int? = null,

    @field:SerializedName("question_content")
    val questionContent: String? =  null,

    @field:SerializedName("option_1")
    val option1: String? = null,

    @field:SerializedName("option_2")
    val option2: String? = null,

    @field:SerializedName("option_3")
    val option3: String? = null,

    @field:SerializedName("option_4")
    val option4: String? = null,
)