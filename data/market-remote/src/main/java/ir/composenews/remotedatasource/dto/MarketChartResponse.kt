package ir.composenews.remotedatasource.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketChartResponse(
    val prices: List<List<Double>>,
    @SerialName("market_caps") val marketCaps: List<List<Double>>,
    @SerialName("total_volumes") val totalVolumes: List<List<Double>>,
)
