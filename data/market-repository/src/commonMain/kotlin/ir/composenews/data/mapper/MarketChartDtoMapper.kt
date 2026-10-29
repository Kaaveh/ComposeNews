@file:Suppress("ktlint")

package ir.composenews.data.mapper

import ir.composenews.domain.model.MarketChart
import ir.composenews.remotedatasource.dto.MarketChartResponse
import kotlinx.collections.immutable.toPersistentList

fun MarketChartResponse.toMarketChart(): MarketChart = MarketChart(
    prices = prices.map { element -> Pair(element[0].toLong(), element[1]) }.toPersistentList(),
)
