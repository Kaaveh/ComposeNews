@file:Suppress("MagicNumber", "ktlint")

package ir.composenews.marketdetail

import ir.composenews.base.LoadableData
import ir.composenews.base.UnidirectionalViewModel
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.uimarket.model.MarketModel

private const val PERCENT = 100

interface MarketDetailContract :
    UnidirectionalViewModel<MarketDetailContract.Event, MarketDetailContract.State> {

    data class State(
        val market: LoadableData<MarketModel> = LoadableData.Initial,
        val marketChart: LoadableData<MarketChart> = LoadableData.Initial,
        val marketDetail: LoadableData<MarketDetail> = LoadableData.Initial,
        val chartRange: ChartRange = ChartRange.Day,
    ) {
        /** Percent change from the first to the last price of the loaded chart; null until it has two points. */
        val rangeChange: Double?
            get() {
                val prices = marketChart.data?.prices ?: return null
                val first = prices.firstOrNull()?.second ?: return null
                val last = prices.last().second
                return if (prices.size < 2 || first == 0.0) null else (last - first) / first * PERCENT
            }
    }

    /** [shortLabel] is the range button, [label] the period next to the change: `1D` and `Today`. */
    enum class ChartRange(val days: Int, val shortLabel: String, val label: String) {
        Day(1, "1D", "Today"),
        Week(7, "1W", "Past week"),
        Month(30, "1M", "Past month"),
        Year(365, "1Y", "Past year"),
    }

    sealed class Event {
        data class SetMarket(val market: MarketModel) : Event()
        data class GetMarketChart(val marketId: String) : Event()
        data class GetMarketDetail(val marketId: String) : Event()
        data class OnFavoriteClick(val market: MarketModel) : Event()
        data class OnChartRangeSelect(val range: ChartRange) : Event()
    }
}
