package ir.kaaveh.favoritenews

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.News

interface FavoriteNewsContract :
    UnidirectionalViewModel<FavoriteNewsContract.Event, FavoriteNewsContract.State> {

    data class State(
        val isLoading: Boolean = false,
        val news: List<News> = listOf(),
        val error: String = "",
    )

    sealed class Event {
        data class OnFavoriteClick(val news: News) : Event()
    }

}