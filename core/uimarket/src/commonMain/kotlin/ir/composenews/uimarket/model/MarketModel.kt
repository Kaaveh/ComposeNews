package ir.composenews.uimarket.model

import kotlinx.serialization.Serializable

@Serializable
data class MarketModel(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val marketCapRank: Int? = null,
    val totalVolume: Double? = null,
    val sparkline7d: List<Double> = emptyList(),
) {
    /** The 7-day trend, first price against last; null without at least two points. */
    val isTrendingUp7d: Boolean?
        get() = if (sparkline7d.size < 2) null else sparkline7d.last() >= sparkline7d.first()
}
