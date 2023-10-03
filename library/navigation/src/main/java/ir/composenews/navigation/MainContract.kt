package ir.composenews.navigation

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.utils.ContentType

interface MainContract :
    UnidirectionalViewModel<MainContract.Event, MainContract.State> {

    data class State(
        val market: MarketModel? = null,
        val isDetailOnlyOpen: Boolean = false,
    )

    sealed class Event {
        data class SetMarket(val market: MarketModel?, val contentType: ContentType) : Event()
    }
}
