package ir.composenews.localdatasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Same table and columns as the SQLDelight `Market.sq` it replaces. Unlike `Market.sq`, name and symbol
 * aren't unique: CoinGecko lists different coins under the same name or symbol, and a unique index
 * silently dropped them.
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
)
