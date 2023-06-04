package ir.kaaveh.marketdetail

import ir.kaaveh.designsystem.UnidirectionalViewModel
import ir.kaaveh.domain.model.Market

interface MarketDetailContract :
    UnidirectionalViewModel<MarketDetailContract.Event, MarketDetailContract.State> {

    data class State(
        val market: Market? = null,
    )

    sealed class Event {
        data class SetMarket(val market: Market?) : Event()
        data class OnFavoriteClick(val market: Market?) : Event()
    }

}