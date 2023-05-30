package ir.kaaveh.remotedatasource.dto

import com.google.gson.annotations.SerializedName

data class MarketResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("total_volume") val totalVolume: Double,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerializedName("image") val imageUrl: String
)