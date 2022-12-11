package ir.kaaveh.remotedatasource.dto

data class NewsResponse(
    val news: List<NewsDto>,
    val status: String,
    val totalResults: Int,
)