@file:Suppress(
    "ktlint:standard:annotation",
    "ktlint:standard:no-empty-first-line-in-class-body",
    "ktlint:standard:function-signature",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.localdatasource.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ir.composenews.db.MarketDatabase
import ir.composenews.db.MarketEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class MarketDaoImpl(
    db: MarketDatabase,
) : MarketDao {

    private val queries = db.marketQueries

    override fun getMarketList(): Flow<List<MarketEntity>> =
        queries.getMarketList().asFlow().mapToList(Dispatchers.IO)

    override fun getFavoriteMarketList(): Flow<List<MarketEntity>> =
        queries.getFavoriteMarketList().asFlow().mapToList(Dispatchers.IO)

    override suspend fun insertMarket(marketEntity: MarketEntity) {
        marketEntity.run {
            queries.insertMarket(
                id = id,
                name = name,
                symbol = symbol,
                currentPrice = currentPrice,
                priceChangePercentage24h = priceChangePercentage24h,
                imageUrl = imageUrl,
                isFavorite = isFavorite,
            )
        }
    }
}
