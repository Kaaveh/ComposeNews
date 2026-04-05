@file:Suppress(
    "ktlint:standard:blank-line-before-declaration",
)

package ir.composenews.domain.repository

import androidx.paging.PagingData
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>
    fun getPagedMarketList(): Flow<PagingData<Market>>
    fun getFavoriteMarketList(): Flow<List<Market>>
    suspend fun syncMarketList()
    suspend fun toggleFavoriteMarket(oldMarket: Market)
    fun fetchChart(id: String): Flow<Resource<MarketChart, Errors>>
    fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>>
}
