package ir.composenews.uimarket.model

data class MarketModel(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
)