@file:Suppress("ktlint")

package ir.composenews.marketlist

import ir.composenews.base.LoadableData
import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.network.Errors
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList

interface MarketListContract :
    UnidirectionalViewModel<MarketListContract.Event, MarketListContract.State> {

    data class State(
        /** The synced top markets after [filter] and [sort]. */
        val marketList: LoadableData<PersistentList<MarketModel>> = LoadableData.Initial,
        val filter: Filter = Filter.All,
        val sort: Sort = Sort.MarketCap,
        /** The biggest 24h gainer of all synced markets, whatever the filter; null when nothing gained. */
        val topMover: MarketModel? = null,
        val isRefreshing: Boolean = false,
        /** Seconds until a rate-limited sync may run again; 0 when it may run now. */
        val retryInSeconds: Int = 0,
        /** The last sync's failure while stored markets stay on screen; cleared when the next sync starts. */
        val syncError: Errors? = null,
    ) {
        val header: Header
            get() = when (filter) {
                Filter.All -> if (sort == Sort.MarketCap) Header.ByMarketCap else Header.ByVolume
                Filter.Gainers -> Header.Gainers
                Filter.Losers -> Header.Losers
            }
    }

    /** Gainers sort by 24h change, highest first; Losers lowest first. Both ignore [Sort]. */
    enum class Filter { All, Gainers, Losers }

    enum class Sort { MarketCap, Volume }

    enum class Header { ByMarketCap, ByVolume, Gainers, Losers }

    sealed class Event {
        /** Starts showing the stored markets and syncs them once; later calls do nothing. */
        data object OnGetMarketList : Event()
        /** Pull to refresh and "Try again": syncs again, unless a 429 countdown is running. */
        data object OnRefresh : Event()
        data class OnFilterSelect(val filter: Filter) : Event()
        data class OnSortSelect(val sort: Sort) : Event()
        data class OnFavoriteClick(val market: MarketModel) : Event()
    }
}
