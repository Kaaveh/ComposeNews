@file:Suppress("ktlint")

package ir.composenews.marketlist

import ir.composenews.base.LoadableData
import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList

interface FavoriteListContract :
    UnidirectionalViewModel<FavoriteListContract.Event, FavoriteListContract.State> {

    data class State(
        val favoriteList: LoadableData<PersistentList<MarketModel>> = LoadableData.Initial,
        val sort: Sort = Sort.Name,
    ) {
        /** For the `5 saved` subtitle. */
        val count: Int
            get() = favoriteList.data?.size ?: 0
    }

    /** Name A to Z; price and 24h change highest first. */
    enum class Sort { Name, Price, Change24h }

    sealed class Event {
        data object OnGetFavoriteList : Event()
        data class OnSortSelect(val sort: Sort) : Event()
        /** Also the swipe to remove. */
        data class OnFavoriteClick(val market: MarketModel) : Event()
    }
}
