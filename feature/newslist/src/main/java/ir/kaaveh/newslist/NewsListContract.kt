package ir.kaaveh.newslist

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.Market

interface NewsListContract :
    UnidirectionalViewModel<NewsListContract.Event, NewsListContract.State> {

    data class State(
        val news: List<Market> = listOf(),
        val refreshing: Boolean = false,
        val showFavoriteList: Boolean = false,
    )

    sealed class Event {
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        data class OnFavoriteClick(val news: Market) : Event()
        object OnGetNewsList : Event()
        object OnRefresh: Event()
    }

}