package ir.composenews.remotedatasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarketChartResponse(
    val prices: List<List<Double>>,
)
