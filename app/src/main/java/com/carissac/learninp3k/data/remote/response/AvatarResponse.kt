package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class AvatarResponseItem(
	@field:SerializedName("avatar_id")
	val avatarId: Int? = null,

	@field:SerializedName("avatar_name")
	val avatarName: String? = null,

	@field:SerializedName("avatar_img")
	val avatarImg: String? = null
)

data class AvatarDetailResponse(
	@field:SerializedName("avatar_id")
	val avatarId: Int? = null,

	@field:SerializedName("avatar_name")
	val avatarName: String? = null,

	@field:SerializedName("avatar_personality")
	val avatarPersonality: String? = null,

	@field:SerializedName("avatar_desc")
	val avatarDesc: String? = null
)