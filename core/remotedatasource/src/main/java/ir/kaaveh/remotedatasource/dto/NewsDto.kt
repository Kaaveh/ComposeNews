package ir.kaaveh.remotedatasource.dto

data class NewsDto(
    val author: String?,
    val description: String?,
    val publishedAt: String?,
    val source: SourceDto?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
)