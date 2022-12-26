package ir.kaaveh.favoritenews

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.News

interface FavoriteNewsContract :
    UnidirectionalViewModel<FavoriteNewsContract.Event, FavoriteNewsContract.State> {

    data class State(
        val news: List<News> = listOf(),
    )

    sealed class Event {
        data class OnFavoriteClick(val news: News) : Event()
    }

}