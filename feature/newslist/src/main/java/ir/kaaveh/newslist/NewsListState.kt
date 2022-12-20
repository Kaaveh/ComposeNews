package ir.kaaveh.newslist

import ir.kaaveh.domain.model.News

data class NewsListState(
    val isLoading: Boolean = false,
    val news: List<News> = listOf(),
    val error: String = "",
)