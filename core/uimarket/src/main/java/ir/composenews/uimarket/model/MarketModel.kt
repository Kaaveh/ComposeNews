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
)
