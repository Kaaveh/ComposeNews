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
)
