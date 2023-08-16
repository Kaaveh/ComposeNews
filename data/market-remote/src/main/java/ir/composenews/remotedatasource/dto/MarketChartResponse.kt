package ir.composenews.remotedatasource.dto

import com.google.gson.annotations.SerializedName

data class MarketChartResponse(
    val prices: List<List<Double>>,
    @SerializedName("market_caps") val marketCaps: List<List<Double>>,
    @SerializedName("total_volumes") val totalVolumes: List<List<Double>>,
)
