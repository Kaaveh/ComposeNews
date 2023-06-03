package ir.kaaveh.data.repository

import ir.kaaveh.data.mapper.toRemoteMarketDto
import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketRepository
import ir.kaaveh.localdatasource.database.MarketDao
import ir.kaaveh.data.mapper.toLocalMarketDto
import ir.kaaveh.data.mapper.toMarket
import ir.kaaveh.remotedatasource.api.MarketsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: MarketsApi,
    private val dao: MarketDao
) : MarketRepository {

    override fun getMarketList(): Flow<List<Market>> =
        dao.getMarketList().map { list -> list.map { it.toMarket() } }

    override fun getFavoriteMarketList(): Flow<List<Market>> =
        dao.getFavoriteMarketList().map { list -> list.map { it.toMarket() } }

    override suspend fun syncMarketList(): Boolean = try {
        api.getMarkets("usd", "market_cap_desc", 20, 1, false)
            .map { it.toRemoteMarketDto() }
            .onEach { dao.upsertMarket(it) }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun toggleFavoriteMarket(oldMarket: Market) {
        val news = oldMarket.toLocalMarketDto().copy(isFavorite = !oldMarket.isFavorite)
        dao.insertMarketList(news)
    }

}