@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.repository

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketChart
import ir.composenews.data.mapper.toMarketDetail
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.localdatasource.database.MarketEntity
import ir.composenews.localdatasource.database.TRUE
import ir.composenews.network.ApiResponse
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class MarketRepositoryImplTest : StringSpec({

    lateinit var marketDao: FakeMarketDao
    lateinit var api: FakeMarketsApi
    lateinit var repository: MarketRepositoryImpl

    beforeTest {
        api = FakeMarketsApi()
        marketDao = FakeMarketDao()
        repository = MarketRepositoryImpl(api = api, dao = marketDao)
    }

    "Given local database has market data, When GetMarketList is called, Then returns mapped market list" {
        val marketEntities = listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE))
        marketDao.markets = flowOf(marketEntities)

        repository.getMarketList().first() shouldBe marketEntities.map { it.toMarket() }
    }

    "Given local database has favorite market data, When GetFavoriteMarketList is called, Then returns mapped favorite market list" {
        val marketEntities = listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE))
        marketDao.favorites = flowOf(marketEntities)

        repository.getFavoriteMarketList().first() shouldBe marketEntities.map { it.toMarket() }
    }

    "Given API provides market data, When SyncMarketList is called, Then updates database with market data" {
        val marketResponse = MarketResponse("1", "Bitcoin", "BTC", 50000.0, 5.0, "url")
        api.markets = { ApiResponse.Success(listOf(marketResponse)) }

        val result = repository.syncMarketList()

        result shouldBe Resource.Success(Unit)
        api.marketsCalls shouldBe listOf(listOf("usd", "market_cap_desc", 250, 1, true))
        marketDao.replaced shouldBe listOf(listOf(marketResponse.toMarketEntity()))
    }

    "Given a sync is running, When SyncMarketList is called again, Then both share one request" {
        val answer = CompletableDeferred<ApiResponse<List<MarketResponse>>>()
        api.markets = { answer.await() }

        val results = coroutineScope {
            val calls = List(2) { async(start = CoroutineStart.UNDISPATCHED) { repository.syncMarketList() } }
            answer.complete(ApiResponse.Success(emptyList()))
            calls.awaitAll()
        }

        results shouldBe List(2) { Resource.Success(Unit) }
        api.marketsCalls.size shouldBe 1
    }

    "Given API call fails, When SyncMarketList is called, Then does not update database" {
        val response = httpResponse(HttpStatusCode.InternalServerError)
        api.markets = { ApiResponse.Failure.Error(response) }

        val result = repository.syncMarketList()

        result shouldBe Resource.Error(Errors.ApiError("Internal Server Error", 500))
        marketDao.replaced.shouldBeEmpty()
    }

    "Given API answers with an empty list, When SyncMarketList is called, Then keeps the stored markets" {
        api.markets = { ApiResponse.Success(emptyList()) }

        repository.syncMarketList() shouldBe Resource.Success(Unit)
        marketDao.replaced.shouldBeEmpty()
    }

    "Given API answers a status the app doesn't name, When SyncMarketList is called, Then keeps the real code" {
        val response = httpResponse(HttpStatusCode.NotFound)
        api.markets = { ApiResponse.Failure.Error(response) }

        repository.syncMarketList() shouldBe Resource.Error(Errors.ApiError("Unknown", 404))
    }

    "Given API answers 429 with Retry-After, When SyncMarketList is called, Then returns RateLimited with that delay" {
        val response = httpResponse(HttpStatusCode.TooManyRequests, retryAfter = "30")
        api.markets = { ApiResponse.Failure.Error(response) }

        repository.syncMarketList() shouldBe Resource.Error(Errors.RateLimited(retryAfterSeconds = 30))
    }

    "Given API answers 429 without Retry-After, When FetchChart is called, Then returns RateLimited after a minute" {
        val response = httpResponse(HttpStatusCode.TooManyRequests)
        api.chart = { ApiResponse.Failure.Error(response) }

        repository.fetchChart("1", 1).first() shouldBe Resource.Error(Errors.RateLimited(retryAfterSeconds = 60))
    }

    "Given an existing market, When ToggleFavoriteMarket is called, Then updates market favorite status" {
        val market = Market("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", false)

        repository.toggleFavoriteMarket(market)

        marketDao.favoriteUpdates shouldBe listOf("1" to TRUE)
    }

    "Given API provides chart data, When FetchChart is called, Then returns mapped MarketChart" {
        val marketChartResponse = MarketChartResponse(listOf(listOf(1000.0, 50000.0)))
        api.chart = { ApiResponse.Success(marketChartResponse) }

        val actualMarkets = repository.fetchChart("1", 7).first()

        actualMarkets shouldBe Resource.Success(marketChartResponse.toMarketChart())
        api.chartCalls shouldBe listOf(Triple("1", "usd", 7))
    }

    "Given API call fails, When FetchChart is called, Then returns error resource" {
        api.chart = { ApiResponse.Failure.Exception(RuntimeException()) }

        repository.fetchChart("1", 7).first().shouldBeInstanceOf<Resource.Error<Errors>>()
    }

    "Given API provides market details, When FetchDetail is called, Then returns mapped MarketDetail" {
        val marketDetailResponse = MarketDetailResponse("1", 1, null, "Bitcoin")
        api.detail = { ApiResponse.Success(marketDetailResponse) }

        val actualMarkets = repository.fetchDetail("1").first()

        actualMarkets shouldBe Resource.Success(marketDetailResponse.toMarketDetail())
        api.detailCalls shouldBe listOf("1")
    }

    "Given API call fails, When FetchDetail is called, Then returns error resource" {
        api.detail = { ApiResponse.Failure.Exception(RuntimeException()) }

        repository.fetchDetail("1").first().shouldBeInstanceOf<Resource.Error<Errors>>()
    }

    "Given a favorited market, When ToggleFavoriteMarket is called, Then updates market favorite status to FALSE" {
        val market = Market("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", true)

        repository.toggleFavoriteMarket(market)

        marketDao.favoriteUpdates shouldBe listOf("1" to FALSE)
    }

    "Given API throws exception, When SyncMarketList is called, Then does not update database" {
        api.markets = { ApiResponse.Failure.Exception(RuntimeException()) }

        repository.syncMarketList().shouldBeInstanceOf<Resource.Error<Errors.ExceptionError>>()

        marketDao.replaced.shouldBeEmpty()
    }

    "Given API provides multiple markets, When SyncMarketList is called, Then all markets are inserted at once" {
        val responses = listOf(
            MarketResponse("1", "Bitcoin", "BTC", 50000.0, 5.0, "url"),
            MarketResponse("2", "Ethereum", "ETH", 3000.0, 2.0, "url"),
        )
        api.markets = { ApiResponse.Success(responses) }

        repository.syncMarketList()

        marketDao.replaced shouldBe listOf(responses.map { it.toMarketEntity() })
    }

    "Given database is empty, When GetMarketList is called, Then returns empty list" {
        repository.getMarketList().first() shouldBe emptyList()
    }
})

/** A real response from ktor's [MockEngine]; the error mappers read its status and headers. */
private suspend fun httpResponse(
    status: HttpStatusCode,
    retryAfter: String? = null,
): HttpResponse {
    val headers = if (retryAfter == null) headersOf() else headersOf(HttpHeaders.RetryAfter, retryAfter)
    return HttpClient(MockEngine { respond(content = "", status = status, headers = headers) }).use { it.get("/") }
}
