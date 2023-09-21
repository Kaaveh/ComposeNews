package ir.composenews.marketlist

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.model.Market
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface MarketListContract :
    UnidirectionalViewModel<MarketListContract.Event, MarketListContract.State> {

    data class State(
        val marketList: PersistentList<Market> = persistentListOf(),
        val refreshing: Boolean = false,
        val showFavoriteList: Boolean = false,
    )

    sealed class Event {
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        data class OnFavoriteClick(val market: Market) : Event()
        data object OnGetMarketList : Event()
        data object OnRefresh : Event()
    }
}
