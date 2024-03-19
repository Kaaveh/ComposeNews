@file:Suppress("MagicNumber", "TooGenericExceptionCaught")

package ir.composenews.data.repository

import android.util.Log
import ir.composenews.data.mapper.toChart
import ir.composenews.data.mapper.toDetail
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.data.mapper.toRemoteMarketDto
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.model.Resource
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.remotedatasource.api.MarketsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class MarketRepositoryImpl @Inject constructor(
    private val api: MarketsApi,
    private val dao: MarketDao,
) : MarketRepository {

    override fun getMarketList(): Flow<List<Market>> =
        dao.getMarketList().map { list -> list.map { it.toMarket() } }

    override fun getFavoriteMarketList(): Flow<List<Market>> =
        dao.getFavoriteMarketList().map { list -> list.map { it.toMarket() } }

    override suspend fun syncMarketList() {
        try {
            val marketList = api.getMarkets(
                "usd",
                "market_cap_desc",
                20,
                1,
                false,
            ).map { it.toRemoteMarketDto() }

            dao.upsertMarket(marketList)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.d("debug", e.toString())
            /* coins/markets API has rate limit. We should decide what should we do in catch block.
               {"status":{"error_code":429,"error_message":"You've exceeded the Rate Limit.
               Please visit https://www.coingecko.com/en/api/pricing to subscribe to our API plans for higher rate limits."}}
             */
        }
    }

    override suspend fun toggleFavoriteMarket(oldMarket: Market) {
        val news = oldMarket.copy(isFavorite = !oldMarket.isFavorite).toMarketEntity()
        dao.insertMarket(news)
    }

    override fun fetchChart(id: String): Flow<Resource<Chart>> = flow {
        try {
            val chart = api.getMarketChart(id, "usd", 1).toChart()
            emit(Resource.Success(chart))
        } catch (e: Exception) {
            emit(Resource.Error(exception = e))
        }
    }

    override fun fetchDetail(id: String): Flow<Resource<MarketDetail>> = flow {
        try {
            val detail = api.getMarketDetail(id).toDetail()
            emit(Resource.Success(detail))
        } catch (e: Exception) {
            emit(Resource.Error(exception = e))
        }
    }
}
