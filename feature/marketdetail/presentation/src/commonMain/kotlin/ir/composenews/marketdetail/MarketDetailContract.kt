@file:Suppress("ktlint")

package ir.composenews.marketdetail

import ir.composenews.base.LoadableData
import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.uimarket.model.MarketModel

interface MarketDetailContract :
    UnidirectionalViewModel<MarketDetailContract.Event, MarketDetailContract.State> {

    data class State(
        val market: LoadableData<MarketModel> = LoadableData.Initial,
        val marketChart: LoadableData<MarketChart> = LoadableData.Initial,
        val marketDetail: LoadableData<MarketDetail> = LoadableData.Initial,
    )

    sealed class Event {
        data class SetMarket(val market: MarketModel) : Event()
        data class GetMarketChart(val marketId: String) : Event()
        data class GetMarketDetail(val marketId: String) : Event()
        data class OnFavoriteClick(val market: MarketModel) : Event()
    }
}
