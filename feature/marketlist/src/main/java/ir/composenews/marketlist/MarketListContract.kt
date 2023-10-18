package ir.composenews.marketlist

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.util.MarketListOrder
import ir.composenews.domain.util.OrderType
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface MarketListContract :
    UnidirectionalViewModel<MarketListContract.Event, MarketListContract.State> {

    data class State(
        val marketList: PersistentList<MarketModel> = persistentListOf(),
        val refreshing: Boolean = false,
        val showFavoriteList: Boolean = false,
        val showFavoriteEmptyState: Boolean = false,
        val showMarketListOrderSection: Boolean = false,
        val marketListOrder: MarketListOrder = MarketListOrder.Price(OrderType.Descending),
    )

    sealed class Event {
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        data class OnFavoriteClick(val market: MarketModel) : Event()
        data object OnGetMarketList : Event()
        data object OnRefresh : Event()
        data class onOrder(val marketListOrder: MarketListOrder) : Event()
    }
}
