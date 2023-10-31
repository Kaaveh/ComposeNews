package ir.composenews.localdatasource.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ir.composenews.db.MarketDatabase
import ir.composenews.db.MarketEntity
import ir.composenews.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketDaoImpl @Inject constructor(
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

    override suspend fun upsertMarket(remoteMarketDto: List<RemoteMarketDto>) {
        remoteMarketDto.forEach { market ->
            market.run {
                queries.upsertMarket(
                    id = id,
                    name = name,
                    symbol = symbol,
                    currentPrice = currentPrice,
                    priceChangePercentage24h = priceChangePercentage24h,
                    imageUrl = imageUrl,
                )
            }
        }
    }
}
