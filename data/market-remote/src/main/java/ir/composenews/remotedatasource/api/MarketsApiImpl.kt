package ir.composenews.remotedatasource.api

import io.ktor.client.HttpClient
import io.ktor.http.appendPathSegments
import ir.composenews.network.ApiResponse
import ir.composenews.network.get
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import ir.composenews.remotedatasource.util.HttpRoutes.COINS
import ir.composenews.remotedatasource.util.HttpRoutes.DAYS
import ir.composenews.remotedatasource.util.HttpRoutes.MARKETS
import ir.composenews.remotedatasource.util.HttpRoutes.MARKET_CHART
import ir.composenews.remotedatasource.util.HttpRoutes.ORDER
import ir.composenews.remotedatasource.util.HttpRoutes.PAGE
import ir.composenews.remotedatasource.util.HttpRoutes.PER_PAGE
import ir.composenews.remotedatasource.util.HttpRoutes.SPARKLINE
import ir.composenews.remotedatasource.util.HttpRoutes.VS_CURRENCY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarketsApiImpl @Inject constructor(
    private val httpClient: HttpClient,
) : MarketsApi {
    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): ApiResponse<List<MarketResponse>> = withContext(Dispatchers.IO) {
        val response = httpClient.get<List<MarketResponse>> {
            url {
                appendPathSegments(COINS, MARKETS)
                parameters.append(VS_CURRENCY, currency)
                parameters.append(ORDER, order)
                parameters.append(PER_PAGE, perPage.toString())
                parameters.append(PAGE, page.toString())
                parameters.append(SPARKLINE, sparkline.toString())
            }
        }
        response
    }

    override suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): ApiResponse<MarketChartResponse> = withContext(Dispatchers.IO) {
        val response = httpClient.get<MarketChartResponse> {
            url {
                appendPathSegments(COINS, id, MARKET_CHART)
                parameters.append(VS_CURRENCY, currency)
                parameters.append(DAYS, days.toString())
            }
        }
        response
    }

    override suspend fun getMarketDetail(id: String): ApiResponse<MarketDetailResponse> =
        withContext(Dispatchers.IO) {
            val response = httpClient.get<MarketDetailResponse> {
                url {
                    appendPathSegments(COINS, id)
                }
            }
            response
        }
}
