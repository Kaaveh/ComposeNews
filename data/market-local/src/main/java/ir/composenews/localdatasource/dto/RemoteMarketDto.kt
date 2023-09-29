package ir.composenews.localdatasource.dto

data class RemoteMarketDto(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
)
