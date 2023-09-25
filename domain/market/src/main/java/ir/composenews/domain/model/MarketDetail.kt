package ir.composenews.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MarketDetail(
    val id: String? = null,
    val marketCapRank: Int? = null,
    val marketData: MarketData? = null,
    val name: String? = null,
) : Parcelable {

    @Immutable
    @Parcelize
    data class MarketData(
        val high24h: High24h? = null,
        val low24h: Low24h? = null,
        val marketCap: MarketCap? = null,
        val marketCapRank: Int? = null,
    ) : Parcelable {
        @Immutable
        @Parcelize
        data class High24h(
            val usd: Double? = null,
        ) : Parcelable

        @Immutable
        @Parcelize
        data class Low24h(
            val usd: Double? = null,
        ) : Parcelable

        @Immutable
        @Parcelize
        data class MarketCap(
            val usd: Long? = null,
        ) : Parcelable
    }
}
