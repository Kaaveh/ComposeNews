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
    @Query("SELECT * FROM marketEntity ORDER BY name ASC")
    fun getMarketList(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM marketEntity WHERE isFavorite = $TRUE ORDER BY name ASC")
    fun getFavoriteMarketList(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM marketEntity WHERE id = :id")
    fun getMarketById(id: String): Flow<MarketEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMarket(marketEntity: MarketEntity)

    @Query(
        "UPDATE marketEntity SET name = :name, symbol = :symbol, currentPrice = :currentPrice, " +
            "priceChangePercentage24h = :priceChangePercentage24h, imageUrl = :imageUrl WHERE id = :id",
    )
    suspend fun updateMarketData(
        id: String,
        name: String,
        symbol: String,
        currentPrice: Double,
        priceChangePercentage24h: Double,
        imageUrl: String,
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
            updateMarketData(it.id, it.name, it.symbol, it.currentPrice, it.priceChangePercentage24h, it.imageUrl)
        }
    }
}
