package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class CourseEnrollmentResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: CourseEnrollmentDetailResponse? = null
)

data class CourseEnrollmentDetailResponse(
    @field:SerializedName("user_course_id")
    val userCourseId: Int? = null,

    @field:SerializedName("progress_status")
    val progressStatus: String? = null,

    @field:SerializedName("course_score")
    val courseScore: Int? = null,

    @field:SerializedName("enrolled_at")
    val enrolledAt: String? = null,

    @field:SerializedName("completed_at")
    val completedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("course_id")
    val courseId: Int? = null,

    @field: SerializedName("updated_at")
    val updatedAt: String? = null,

    @field: SerializedName("created_at")
    val createdAt: String? = null
)