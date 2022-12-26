package ir.kaaveh.newsdetail

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.designsystem.UnidirectionalViewModelWithEffect
import ir.kaaveh.domain.model.News

interface NewsDetailContract :
    UnidirectionalViewModel<NewsDetailContract.Event, NewsDetailContract.State> {

    data class State(
        val news: News? = null,
    )

    sealed class Event {
        data class SetNews(val news: News?) : Event()
        data class OnFavoriteClick(val news: News?) : Event()
    }

}