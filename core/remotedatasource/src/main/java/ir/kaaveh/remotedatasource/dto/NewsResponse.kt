package ir.kaaveh.remotedatasource.dto

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val news: List<NewsDto>,
    val status: String?,
    val totalResults: Int?,
)