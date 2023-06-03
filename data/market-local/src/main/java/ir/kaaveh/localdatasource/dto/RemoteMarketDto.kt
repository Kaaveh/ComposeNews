package ir.kaaveh.localdatasource.dto

data class RemoteMarketDto(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
)
