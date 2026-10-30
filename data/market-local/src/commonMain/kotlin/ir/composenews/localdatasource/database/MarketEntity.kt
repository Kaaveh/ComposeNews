package ir.composenews.localdatasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Same table and columns as the SQLDelight `Market.sq` it replaces. Unlike `Market.sq`, name and symbol
 * aren't unique: CoinGecko lists different coins under the same name or symbol, and a unique index
 * silently dropped them.
 *
 * Version 2 adds [marketCapRank], [totalVolume] and [sparkline7d]. They're null on rows from before it,
 * and [marketCapRank] is null on rows outside the latest top markets (see [MarketDao.replaceTopMarkets]).
 */
@Entity(tableName = "marketEntity")
data class MarketEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
    val isFavorite: Long,
    val marketCapRank: Int? = null,
    val totalVolume: Double? = null,
    /** The 7-day prices, comma separated. */
    val sparkline7d: String? = null,
)
