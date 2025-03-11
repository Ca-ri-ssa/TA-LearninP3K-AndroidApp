package com.carissac.learninp3k.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AvatarResponseItem(
	@field:SerializedName("avatar_id")
	val avatarId: Int? = null,

	@field:SerializedName("avatar_name")
	val avatarName: String? = null,

	@field:SerializedName("avatar_img")
	val avatarImg: String? = null
): Parcelable

data class AvatarDetailResponse(
	@field:SerializedName("avatar_id")
	val avatarId: Int? = null,

	@field:SerializedName("avatar_name")
	val avatarName: String? = null,

	@field:SerializedName("avatar_img")
	val avatarImg: String? = null,

	@field:SerializedName("avatar_personality")
	val avatarPersonality: String? = null,

	@field:SerializedName("avatar_desc")
	val avatarDesc: String? = null
)