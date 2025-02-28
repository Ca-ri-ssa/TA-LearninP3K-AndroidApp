package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class CourseIntroResponse (
    @field:SerializedName("course_id")
    val courseId: Int? = null,

    @field:SerializedName("course_name")
    val courseName: String? = null,

    @field:SerializedName("course_level")
    val courseLevel: String? = null,

    @field:SerializedName("course_intro")
    val courseIntro: String? = null,

    @field:SerializedName("course_goal")
    val courseGoal: String? = null,

    @field:SerializedName("course_img")
    val courseImg: String? = null,

    @field:SerializedName("is_enrolled")
    val isEnrolled: Boolean? = null,

    @field:SerializedName("progress_status")
    val progressStatus: String? = null,

    @field:SerializedName("course_score")
    val courseScore: Int? = null,

    @field:SerializedName("enrolled_at")
    val enrolledAt: String? = null,

    @field:SerializedName("completed_at")
    val completedAt: String? = null
)

data class CourseDetailResponse(
    @field:SerializedName("course_id")
    val courseId: Int? = null,

    @field:SerializedName("course_name")
    val courseName: String? = null,

    @field:SerializedName("course_content")
    val courseContent: String? = null,

    @field:SerializedName("course_vid")
    val courseVid: String? = null,

    @field:SerializedName("vid_source")
    val vidSource: String? = null,

    @field:SerializedName("vid_source_creator")
    val vidSourceCreator: String? = null,

    @field:SerializedName("img_course_heading")
    val imgCourseHeading: String? = null,

    @field:SerializedName("img_course")
    val imgCourse: List<ImgCourseResponseItem>? = null
)

data class ImgCourseResponseItem(
    @field:SerializedName("img_course_id")
    val imgCourseId: Int? = null,

    @field:SerializedName("img_course_name")
    val imgCourseName: String? = null,

    @field:SerializedName("img_course_url")
    val imgCourseUrl: String? = null
)