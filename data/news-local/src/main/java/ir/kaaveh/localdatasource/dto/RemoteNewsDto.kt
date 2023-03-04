package ir.kaaveh.localdatasource.dto

data class RemoteNewsDto(
    val title: String,
    val author: String,
    val description: String,
    val publishedAt: String,
    val source: String,
    val url: String,
    val urlToImage: String,
)
