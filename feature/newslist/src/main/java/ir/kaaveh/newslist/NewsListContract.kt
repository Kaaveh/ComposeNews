package ir.kaaveh.newslist

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.News

interface NewsListContract :
    UnidirectionalViewModel<NewsListContract.Event, NewsListContract.State> {

    data class State(
        val news: List<News> = listOf(),
    )

    sealed class Event {
        data class OnFavoriteClick(val news: News) : Event()
    }

}