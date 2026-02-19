@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.repository

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Ordering
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketChart
import ir.composenews.data.mapper.toMarketDetail
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.db.MarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.TRUE
import ir.composenews.network.ApiResponse
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class MarketRepositoryImplTest : StringSpec({

    lateinit var marketDao: MarketDao
    lateinit var api: MarketsApi
    lateinit var repository: MarketRepositoryImpl

    beforeTest {
        api = mockk(relaxed = true)
        marketDao = mockk(relaxed = true)
        repository = MarketRepositoryImpl(api = api, dao = marketDao)
    }

    "Given local database has market data, When GetMarketList is called, Then returns mapped market list" {
        val marketEntities = listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE))
        val expectedMarkets = marketEntities.map { it.toMarket() }
        every { marketDao.getMarketList() } returns flowOf(marketEntities)

        val actualMarkets = repository.getMarketList().first()

        actualMarkets shouldBe expectedMarkets
    }

    "Given local database has favorite market data, When GetFavoriteMarketList is called, Then returns mapped favorite market list" {
        val marketEntities = listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE))
        val expectedMarkets = marketEntities.map { it.toMarket() }
        every { marketDao.getFavoriteMarketList() } returns flowOf(marketEntities)

        val actualMarkets = repository.getFavoriteMarketList().first()

        actualMarkets shouldBe expectedMarkets
    }

    "Given API provides market data, When SyncMarketList is called, Then updates database with market data" {
        val marketResponses = MarketResponse("1", "Bitcoin", "BTC", 50000.0, 5.0, "url")
        val marketEntities = marketResponses.toMarketEntity()
        coEvery { api.getMarkets(any(), any(), any(), any(), any()) } returns ApiResponse.Success(
            listOf(marketResponses),
        )
        coEvery { marketDao.insertMarket(marketEntities) } just Runs

        repository.syncMarketList()

        coVerify(ordering = Ordering.SEQUENCE) {
            api.getMarkets(any(), any(), any(), any(), any())
            marketDao.insertMarket(marketEntities)
        }
    }

    "Given API call fails, When SyncMarketList is called, Then does not update database" {
        coEvery {
            api.getMarkets(any(), any(), any(), any(), any())
        } returns ApiResponse.Failure.Error(mockk())

        repository.syncMarketList()

        coVerify(exactly = 0) { marketDao.insertMarket(any()) }
    }

    "Given an existing market, When ToggleFavoriteMarket is called, Then updates market favorite status" {
        val market = Market("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", false)
        coEvery { marketDao.updateFavoriteStatus(any(), any()) } just Runs

        repository.toggleFavoriteMarket(market)

        coVerify(exactly = 1) {
            marketDao.updateFavoriteStatus(id = "1", isFavorite = TRUE)
        }
    }

    "Given API provides chart data, When FetchChart is called, Then returns mapped MarketChart" {
        val marketChartResponse = MarketChartResponse(listOf(listOf(1000.0, 50000.0)))
        coEvery { api.getMarketChart("1", "usd", 1) } returns ApiResponse.Success(
            marketChartResponse,
        )

        val actualMarkets = repository.fetchChart("1").first()

        actualMarkets shouldBe Resource.Success(marketChartResponse.toMarketChart())
    }

    "Given API call fails, When FetchChart is called, Then returns error resource" {
        coEvery { api.getMarketChart("1", "usd", 1) } returns ApiResponse.Failure.Exception(
            IOException(),
        )

        val actualMarkets = repository.fetchChart("1").first()

        actualMarkets.shouldBeInstanceOf<Resource.Error<Errors>>()
    }

    "Given API provides market details, When FetchDetail is called, Then returns mapped MarketDetail" {
        val marketDetailResponse = MarketDetailResponse("1", 1, null, "Bitcoin")
        coEvery { api.getMarketDetail("1") } returns ApiResponse.Success(marketDetailResponse)

        val actualMarkets = repository.fetchDetail("1").first()

        actualMarkets shouldBe Resource.Success(marketDetailResponse.toMarketDetail())
    }

    "Given API call fails, When FetchDetail is called, Then returns error resource" {
        coEvery { api.getMarketDetail("1") } returns ApiResponse.Failure.Exception(IOException())

        val actualMarkets = repository.fetchDetail("1").first()

        actualMarkets.shouldBeInstanceOf<Resource.Error<Errors>>()
    }
})
