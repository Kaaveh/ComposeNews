@file:Suppress("MagicNumber", "TooGenericExceptionCaught")

package ir.composenews.data.repository

import ir.composenews.data.mapper.toChart
import ir.composenews.data.mapper.toLocalMarketDto
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toRemoteMarketDto
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.Resource
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.remotedatasource.api.MarketsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: MarketsApi,
    private val dao: MarketDao,
) : MarketRepository {

    override fun getMarketList(): Flow<List<Market>> =
        dao.getMarketList().map { list -> list.map { it.toMarket() } }

    override fun getFavoriteMarketList(): Flow<List<Market>> =
        dao.getFavoriteMarketList().map { list -> list.map { it.toMarket() } }

    override suspend fun syncMarketList() {
        val marketList = api.getMarkets("usd", "market_cap_desc", 20, 1, false)
            .map { it.toRemoteMarketDto() }

        dao.upsertMarket(marketList)
    }

    override suspend fun toggleFavoriteMarket(oldMarket: Market) {
        val news = oldMarket.toLocalMarketDto().copy(isFavorite = !oldMarket.isFavorite)
        dao.insertMarketList(news)
    }

    override fun fetchChart(id: String): Flow<Resource<Chart>> = flow {
        try {
            val chart = api.getMarketChart(id, "usd", 1).toChart()
            emit(Resource.Success(chart))
        } catch (e: Exception) {
            emit(Resource.Error(exception = e))
        }
    }
}
