package ir.composenews.marketdetail

import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketDetail

interface MarketDetailContract :
    UnidirectionalViewModel<MarketDetailContract.Event, MarketDetailContract.State> {

    data class State(
        val market: Market? = null,
        val loading: Boolean = true,
        val refreshing: Boolean = false,
        val marketChart: Chart = Chart(listOf()),
        val marketDetail: MarketDetail = MarketDetail(),
    )

    sealed class Event {
        data class SetMarket(val market: Market?) : Event()
        data class GetMarketChart(val marketId: String) : Event()
        data class GetMarketDetail(val marketId: String) : Event()
        data class OnFavoriteClick(val market: Market?) : Event()
    }

}