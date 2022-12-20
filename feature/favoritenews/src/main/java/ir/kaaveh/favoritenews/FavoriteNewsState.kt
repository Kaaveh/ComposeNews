package ir.kaaveh.favoritenews

import ir.kaaveh.domain.model.News

data class FavoriteNewsState(
    val isLoading: Boolean = false,
    val news: List<News> = listOf(),
    val error: String = "",
)
