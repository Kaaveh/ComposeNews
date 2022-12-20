package ir.kaaveh.newsdetail

import ir.kaaveh.designsystem.UnidirectionalViewModelWithEffect
import ir.kaaveh.domain.model.News

interface NewsDetailContract :
    UnidirectionalViewModelWithEffect<NewsDetailContract.Event, NewsDetailContract.Effect, NewsDetailContract.State> {

    data class State(
        val isLoading: Boolean = false,
        val news: News? = null,
        val error: String = "",
    )

    sealed class Event {
        data class SetNews(val news: News?) : Event()
        data class OnFavoriteClick(val news: News?) : Event()
        object OnBackPressed : Event()
    }

    sealed class Effect {
        object OnBackPressed : Effect()
    }
}