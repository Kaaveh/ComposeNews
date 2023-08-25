package ir.composenews.marketlist

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.model.Market

interface MarketListContract :
    UnidirectionalViewModel<MarketListContract.Event, MarketListContract.State> {

    data class State(
        val marketList: List<Market> = listOf(),
        val refreshing: Boolean = false,
        val showFavoriteList: Boolean = false,
    )

    sealed class Event {
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        data class OnFavoriteClick(val market: Market) : Event()
        data object OnGetMarketList : Event()
        data object OnRefresh: Event()
    }

}