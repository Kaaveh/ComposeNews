@file:Suppress(
    "ktlint:standard:no-empty-first-line-in-class-body",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.localdatasource.database

import ir.composenews.db.MarketEntity
import kotlinx.coroutines.flow.Flow

interface MarketDao {

    fun getMarketList(): Flow<List<MarketEntity>>

    fun getFavoriteMarketList(): Flow<List<MarketEntity>>

    suspend fun insertMarket(marketEntity: MarketEntity)

    suspend fun updateFavoriteStatus(
        id: String,
        isFavorite: Long,
    )
}
