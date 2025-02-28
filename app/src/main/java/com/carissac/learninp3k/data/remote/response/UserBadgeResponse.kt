package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserBadgeResponse (
    @field:SerializedName("badge_id")
    val badgeId: Int? = null,

    @field:SerializedName("badge_name")
    val badgeName: String? = null,

    @field:SerializedName("badge_desc")
    val badgeDesc: String? = null,

    @field:SerializedName("badge_img")
    val badgeImg: String? = null,

    @field:SerializedName("badge_category")
    val badgeCategory: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null
)

data class UserBadgeItemResponse (
    @field:SerializedName("badge_id")
    val badgeId: Int? = null,

    @field:SerializedName("badge_name")
    val badgeName: String? = null,

    @field:SerializedName("badge_img")
    val badgeImg: String? = null,

    @field:SerializedName("badge_category")
    val badgeCategory: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null
)
