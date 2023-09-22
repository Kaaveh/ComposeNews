package ir.composenews.remotedatasource.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketDetailResponse(
    @SerialName("id")
    val id: String? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,
    @SerialName("market_data")
    val marketData: MarketData? = null,
    @SerialName("name")
    val name: String? = null,
) {

    @Serializable
    data class MarketData(
        @SerialName("high_24h")
        val high24h: High24h? = null,
        @SerialName("low_24h")
        val low24h: Low24h? = null,
        @SerialName("market_cap")
        val marketCap: MarketCap? = null,
        @SerialName("market_cap_rank")
        val marketCapRank: Int? = null,
    ) {
        @Serializable
        data class High24h(
            @SerialName("usd")
            val usd: Double? = null,
        )

        @Serializable
        data class Low24h(
            @SerialName("usd")
            val usd: Double? = null,
        )

        @Serializable
        data class MarketCap(
            @SerialName("usd")
            val usd: Long? = null,
        )
    }
}