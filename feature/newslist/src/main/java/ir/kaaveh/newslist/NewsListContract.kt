package ir.kaaveh.newslist

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.News

interface NewsListContract :
    UnidirectionalViewModel<NewsListContract.Event, NewsListContract.Effect, NewsListContract.State> {

    data class State(
        val isLoading: Boolean = false,
        val news: List<News> = listOf(),
        val error: String = "",
    )

    sealed class Event {
        data class OnFavoriteClick(val news: News) : Event()
    }

    sealed class Effect

}