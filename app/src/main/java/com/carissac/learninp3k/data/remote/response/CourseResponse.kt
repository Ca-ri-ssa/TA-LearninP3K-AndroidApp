package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class CourseResponse (
    @field:SerializedName("total_course")
    val totalCourse: Int? = null,

    @field:SerializedName("courses")
    val courses: List<CourseResponseItem>? = null
)

data class CourseResponseItem (
    @field:SerializedName("course_id")
    val courseId: Int? = null,

    @field:SerializedName("course_name")
    val courseName: String? = null,

    @field:SerializedName("course_level")
    val courseLevel: String? = null,

    @field:SerializedName("course_intro")
    val courseIntro: String? = null,

    @field:SerializedName("course_thumbnail")
    val courseThumbnail: String? = null,

    @field:SerializedName("is_enrolled")
    val isEnrolled: Boolean? = null,

    @field:SerializedName("progress_status")
    val progressStatus: String? = null

)