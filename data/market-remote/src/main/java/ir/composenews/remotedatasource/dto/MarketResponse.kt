package ir.composenews.remotedatasource.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketResponse(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("current_price") val currentPrice: Double,
    @SerialName("market_cap") val marketCap: Double,
    @SerialName("total_volume") val totalVolume: Double,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerialName("image") val imageUrl: String,
)
