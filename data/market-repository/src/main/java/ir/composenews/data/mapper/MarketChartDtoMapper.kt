package ir.composenews.data.mapper

import ir.composenews.domain.model.Chart
import ir.composenews.remotedatasource.dto.MarketChartResponse
import kotlinx.collections.immutable.toPersistentList

fun MarketChartResponse.toChart(): Chart = Chart(
    prices = prices.map { pair -> Pair(pair[0].toInt(), pair[1]) }.toPersistentList()
)