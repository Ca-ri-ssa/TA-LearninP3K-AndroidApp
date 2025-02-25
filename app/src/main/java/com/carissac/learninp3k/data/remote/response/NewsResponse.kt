package com.carissac.learninp3k.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchNewsResponse(
    @field:SerializedName("total_result")
    val totalResult: Int? = null,

    @field:SerializedName("news")
    val news: List<NewsResponseItem>? = null
)

data class NewsResponseItem(
    @field:SerializedName("news_id")
    val newsId: Int? = null,

    @field:SerializedName("news_title")
    val newsTitle: String? = null,

    @field:SerializedName("news_content")
    val newsContent: String? = null,

    @field:SerializedName("news_img")
    val newsImg: String? = null,

    @field:SerializedName("published_at")
    val publishedAt: String? = null
)