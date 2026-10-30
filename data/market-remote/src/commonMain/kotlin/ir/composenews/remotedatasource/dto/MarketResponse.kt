package ir.composenews.remotedatasource.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketResponse(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("current_price") val currentPrice: Double? = null,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerialName("image") val imageUrl: String,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("total_volume") val totalVolume: Double? = null,
    @SerialName("sparkline_in_7d") val sparklineIn7d: Sparkline? = null,
) {
    @Serializable
    data class Sparkline(
        val price: List<Double>,
    )
}
