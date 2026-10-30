package ir.composenews.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Market(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    /** Null once the market drops out of the synced top markets. */
    val marketCapRank: Int? = null,
    val totalVolume: Double? = null,
    /** Hourly prices over the last 7 days, oldest first; empty until a sync fetches them. */
    val sparkline7d: List<Double> = emptyList(),
)
