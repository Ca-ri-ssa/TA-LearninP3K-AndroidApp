package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class WeeklyChallengeResponse (
    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("challenge")
    val challenge: WeeklyChallengeDetailResponse? = null
)

data class WeeklyChallengeDetailResponse (
    @field:SerializedName("challenge_id")
    val challengeId: Int? = null,

    @field:SerializedName("challenge_name")
    val challengeName: String? = null,

    @field:SerializedName("challenge_level")
    val challengeLevel: String? = null,

    @field:SerializedName("challenge_img")
    val challengeImg: String? = null,

    @field:SerializedName("challenge_question")
    val challengeQuestion: String? = null,

    @field:SerializedName("option_1")
    val option1: String? = null,

    @field:SerializedName("option_2")
    val option2: String? = null,

    @field:SerializedName("option_3")
    val option3: String? = null,

    @field:SerializedName("option_4")
    val option4: String? = null,

    @field:SerializedName("challenge_answer")
    val challengeAnswer: String? = null,

    @field:SerializedName("challenge_badge_name")
    val challengeBadgeName: String? = null,

    @field:SerializedName("challenge_badge_img")
    val challengeBadgeImg: String? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null
)

data class TakeWeeklyChallengeResponse (
    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("challenge_name")
    val challengeName: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("earned_point")
    val earnedPoint: Int? = null,

    @field:SerializedName("badge_name")
    val badgeName: String? = null,

    @field:SerializedName("badge_img")
    val badgeImg: String? = null,
)