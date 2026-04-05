@file:Suppress("TooGenericExceptionCaught")

package ir.composenews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.network.onError
import ir.composenews.network.onException
import ir.composenews.network.suspendOnSuccess
import ir.composenews.remotedatasource.api.MarketsApi

private const val PAGE_SIZE = 20

class MarketsPagingSource(
    private val api: MarketsApi,
    private val dao: MarketDao,
) : PagingSource<Int, Market>() {
    override fun getRefreshKey(state: PagingState<Int, Market>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Market> {
        val page = params.key ?: 1
        Log.d("MarketsPaging", "Loading page: $page")
        return try {
            var result: LoadResult<Int, Market> =
                LoadResult.Error(Exception("Unknown error"))
            api
                .getMarkets(
                    currency = "usd",
                    order = "market_cap_desc",
                    perPage = PAGE_SIZE,
                    page = page,
                    sparkline = false,
                ).suspendOnSuccess {
                    val markets = data.map { it.toMarket() }
                    data.forEach { dao.insertMarket(it.toMarketEntity()) }
                    Log.d("MarketsPaging", "Page $page loaded: ${markets.size} items, nextKey=${if (markets.isEmpty()) null else page + 1}")
                    result =
                        LoadResult.Page(
                            data = markets,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (markets.isEmpty()) null else page + 1,
                        )
                }.onError {
                    Log.e("MarketsPaging", "Page $page error: $message")
                    result = LoadResult.Error(Exception(message))
                }.onException {
                    Log.e("MarketsPaging", "Page $page exception: $message", throwable)
                    result = LoadResult.Error(throwable)
                }
            result
        } catch (e: Exception) {
            Log.e("MarketsPaging", "Page $page caught exception: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}
