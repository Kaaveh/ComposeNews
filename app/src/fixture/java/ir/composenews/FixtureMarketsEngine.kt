package ir.composenews

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.headersOf
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import kotlinx.serialization.json.Json

/**
 * Answers the CoinGecko routes with fixed JSON, so the benchmark build runs the real MarketsApiImpl,
 * Ktor client and JSON decoding (and the baseline profile covers them) without the network.
 * The data is fixed so benchmarks stay deterministic.
 */
@Suppress("MagicNumber")
internal object FixtureMarketsEngine {
    // There are 60 entries so the LazyColumn is
    // guaranteed to be scrollable.
    private val markets =
        (1..60).map { index ->
            MarketResponse(
                id = "market-$index",
                name = "Market $index",
                symbol = "m$index",
                currentPrice = 1_000.0 + index,
                priceChangePercentage24h = index / 10.0,
                imageUrl = "",
                marketCapRank = index,
                totalVolume = 1_000_000.0 * index,
                sparklineIn7d = MarketResponse.Sparkline(List(7) { day -> 1_000.0 + index + day }),
            )
        }

    private val chart =
        MarketChartResponse(
            prices =
                listOf(
                    listOf(1_000.0, 50_000.0),
                    listOf(2_000.0, 51_000.0),
                ),
        )

    private fun detail(id: String) =
        MarketDetailResponse(
            id = id,
            marketCapRank = 1,
            name = "Fixture Market",
            marketData =
                MarketDetailResponse.MarketData(
                    high24h = MarketDetailResponse.MarketData.High24h(51_000.0),
                    low24h = MarketDetailResponse.MarketData.Low24h(49_000.0),
                    marketCap = MarketDetailResponse.MarketData.MarketCap(1_000_000_000L),
                    marketCapRank = 1,
                ),
        )

    private fun page(parameters: Parameters): List<MarketResponse> {
        val perPage = parameters["per_page"]?.toIntOrNull() ?: markets.size
        val page = parameters["page"]?.toIntOrNull() ?: 1
        return markets.drop(((page - 1) * perPage).coerceAtLeast(0)).take(perPage)
    }

    fun create(): HttpClientEngine =
        MockEngine { request ->
            val path = request.url.encodedPath
            val body =
                when {
                    path.endsWith("/coins/markets") -> Json.encodeToString(page(request.url.parameters))
                    path.endsWith("/market_chart") -> Json.encodeToString(chart)
                    else -> Json.encodeToString(detail(path.substringAfterLast('/')))
                }
            respond(body, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
        }
}
