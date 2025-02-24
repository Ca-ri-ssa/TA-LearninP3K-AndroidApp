package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
	@field:SerializedName("avatar_img")
	val avatar: String? = null,

	@field:SerializedName("user_email")
	val email: String? = null,

	@field:SerializedName("user_id")
	val id: Int? = null,

	@field:SerializedName("user_name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("total_point")
	val totalPoint: Int? = null
)

data class ProfileResultResponse(
	@field:SerializedName("msg")
	val msg: String? = null
)

