package ir.kaaveh.data.repository

import ir.kaaveh.data.mapper.toRemoteNewsDto
import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketsRepository
import ir.kaaveh.localdatasource.database.MarketDao
import ir.kaaveh.data.mapper.toLocalNewsDto
import ir.kaaveh.data.mapper.toNews
import ir.kaaveh.remotedatasource.api.MarketsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarketsRepositoryImpl @Inject constructor(
    private val api: MarketsApi,
    private val dao: MarketDao
) : MarketsRepository {

    override fun getMarkets(): Flow<List<Market>> =
        dao.getMarketList().map { list -> list.map { it.toNews() } }

    override fun getFavoriteMarkets(): Flow<List<Market>> =
        dao.getFavoriteMarketList().map { list -> list.map { it.toNews() } }

    override suspend fun syncMarkets(): Boolean = try {
        api.getMarkets("usd", "market_cap_desc", 20, 1, false)
            .map { it.toRemoteNewsDto() }
            .onEach { dao.upsertMarket(it) }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun toggleFavoriteMarkets(oldMarket: Market) {
        val news = oldMarket.toLocalNewsDto().copy(isFavorite = !oldMarket.isFavorite)
        dao.insertMarketList(news)
    }

}