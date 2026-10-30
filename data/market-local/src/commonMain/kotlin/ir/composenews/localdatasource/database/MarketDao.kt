@file:Suppress("LongParameterList")

package ir.composenews.localdatasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {
    /** The latest top markets, by market cap. */
    @Query("SELECT * FROM marketEntity WHERE marketCapRank IS NOT NULL ORDER BY marketCapRank ASC")
    fun getMarketList(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM marketEntity WHERE isFavorite = $TRUE ORDER BY name ASC")
    fun getFavoriteMarketList(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM marketEntity WHERE id = :id")
    fun getMarketById(id: String): Flow<MarketEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMarket(marketEntity: MarketEntity)

    @Query(
        "UPDATE marketEntity SET name = :name, symbol = :symbol, currentPrice = :currentPrice, " +
            "priceChangePercentage24h = :priceChangePercentage24h, imageUrl = :imageUrl, " +
            "marketCapRank = :marketCapRank, totalVolume = :totalVolume, sparkline7d = :sparkline7d WHERE id = :id",
    )
    suspend fun updateMarketData(
        id: String,
        name: String,
        symbol: String,
        currentPrice: Double,
        priceChangePercentage24h: Double,
        imageUrl: String,
        marketCapRank: Int?,
        totalVolume: Double?,
        sparkline7d: String?,
    )

    @Query("UPDATE marketEntity SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(
        id: String,
        isFavorite: Long,
    )

    /** Inserts new markets and refreshes existing ones without touching their favorite flag, in one transaction. */
    @Transaction
    suspend fun insertMarkets(marketEntities: List<MarketEntity>) {
        marketEntities.forEach {
            insertOrIgnoreMarket(it)
            updateMarketData(
                it.id,
                it.name,
                it.symbol,
                it.currentPrice,
                it.priceChangePercentage24h,
                it.imageUrl,
                it.marketCapRank,
                it.totalVolume,
                it.sparkline7d,
            )
        }
    }

    @Query("UPDATE marketEntity SET marketCapRank = NULL")
    suspend fun clearMarketCapRanks()

    /**
     * Stores the latest top markets. Markets that dropped out lose their rank, so [getMarketList] no longer
     * lists them, but keep their row and favorite flag.
     */
    @Transaction
    suspend fun replaceTopMarkets(marketEntities: List<MarketEntity>) {
        clearMarketCapRanks()
        insertMarkets(marketEntities)
    }
}
