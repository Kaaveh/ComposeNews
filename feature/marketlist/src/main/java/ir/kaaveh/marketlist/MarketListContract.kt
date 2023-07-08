package ir.kaaveh.marketlist

import ir.kaaveh.base.UnidirectionalViewModel
import ir.kaaveh.domain.model.Market

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
        object OnGetMarketList : Event()
        object OnRefresh: Event()
    }

}