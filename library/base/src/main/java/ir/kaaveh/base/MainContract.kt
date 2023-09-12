package ir.kaaveh.base

import ir.kaaveh.designsystem.utils.ContentType

interface MainContract :
    UnidirectionalViewModel<MainContract.Event, MainContract.State> {

    data class State(
        val market: Any? = null,
        val isDetailOnlyOpen: Boolean = false
    )

    sealed class Event {
        data class SetMarket(val market: Any?, val contentType: ContentType) : Event()
    }

}