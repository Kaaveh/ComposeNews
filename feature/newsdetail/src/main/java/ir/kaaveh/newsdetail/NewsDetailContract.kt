package ir.kaaveh.newsdetail

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.Market

interface NewsDetailContract :
    UnidirectionalViewModel<NewsDetailContract.Event, NewsDetailContract.State> {

    data class State(
        val news: Market? = null,
    )

    sealed class Event {
        data class SetNews(val news: Market?) : Event()
        data class OnFavoriteClick(val news: Market?) : Event()
    }

}