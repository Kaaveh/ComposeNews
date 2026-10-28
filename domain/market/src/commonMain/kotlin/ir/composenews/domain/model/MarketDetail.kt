package ir.composenews.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MarketDetail(
    val id: String,
    val marketCapRank: Int,
    val marketData: MarketData?,
    val name: String,
) {
    @Serializable
    data class MarketData(
        val high24hUSD: Double,
        val low24hUSD: Double,
        val marketCapUSD: Long,
        val marketCapRank: Int,
    )
}
