package ir.kaaveh.data.mapper

import ir.composenews.domain.model.Chart
import ir.kaaveh.remotedatasource.dto.MarketChartResponse

fun MarketChartResponse.toChart(): Chart = Chart(
    prices = prices.map { pair -> Pair(pair[0].toInt(), pair[1]) }
)