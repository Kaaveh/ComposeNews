@file:Suppress("ktlint")

package ir.composenews.marketlist

import ir.composenews.base.LoadableData
import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList

interface MarketListContract :
    UnidirectionalViewModel<MarketListContract.Event, MarketListContract.State> {

    data class State(
        val favoriteMarketList: LoadableData<PersistentList<MarketModel>> = LoadableData.Initial,
        val showFavoriteList: Boolean = false,
    )

    sealed class Event {
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        data class OnFavoriteClick(val market: MarketModel) : Event()
        data object OnGetMarketList : Event()
    }
}
