package ir.composenews.appwatch.navigation

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.uimarket.model.MarketModel

interface MainContract :
    UnidirectionalViewModel<MainContract.Event, MainContract.State> {

    data class State(
        val market: MarketModel? = null,
    )

    sealed class Event {
        data class SetMarket(val market: MarketModel?) : Event()
    }
}
