package ir.composenews.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketDetail(
    val id: String? = null,
    val marketCapRank: Int? = null,
    val marketData: MarketData? = null,
    val name: String? = null,
) : Parcelable {

    @Parcelize
    data class MarketData(
        val high24h: High24h? = null,
        val low24h: Low24h? = null,
        val marketCap: MarketCap? = null,
        val marketCapRank: Int? = null,
    ) : Parcelable {
        @Parcelize
        data class High24h(
            val usd: Double? = null,
        ) : Parcelable

        @Parcelize
        data class Low24h(
            val usd: Double? = null,
        ) : Parcelable

        @Parcelize
        data class MarketCap(
            val usd: Long? = null,
        ) : Parcelable
    }
}
