package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse (
    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("user_name")
    val userName: String? = null,

    @field:SerializedName("avatar_img")
    val avatarImg: String? = null,

    @field:SerializedName("total_point")
    val totalPoint: Int? = null
)

data class UserLeaderboardResponse (
    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("user_name")
    val userName: String? = null,

    @field:SerializedName("avatar_img")
    val avatarImg: String? = null,

    @field:SerializedName("total_point")
    val totalPoint: Int? = null,

    @field:SerializedName("badge_count")
    val badgeCount: Int? = null
)