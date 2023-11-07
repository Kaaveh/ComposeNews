package ir.composenews.marketdetail

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.composenews.base.BaseContract
import ir.composenews.core_test.MainCoroutineListener
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Resource
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.net.SocketTimeoutException
import java.util.UUID
import kotlin.random.Random

class MarketDetailViewModelTest : StringSpec({
    val getMarketChartUseCase: GetMarketChartUseCase = mockk(relaxed = true)
    val getMarketDetailUseCase: GetMarketDetailUseCase = mockk(relaxed = true)
    val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase = mockk(relaxed = true)
    val testScheduler = TestCoroutineScheduler()
    val dispatcherProvider = TestDispatcherProvider(testScheduler)

    lateinit var sut: MarketDetailViewModel

    val dispatcher = UnconfinedTestDispatcher()

    listeners(MainCoroutineListener(dispatcher))

    beforeSpec {
        sut = MarketDetailViewModel(
            getMarketChartUseCase = getMarketChartUseCase,
            getMarketDetailUseCase = getMarketDetailUseCase,
            toggleFavoriteMarketListUseCase = toggleFavoriteMarketListUseCase,
            dispatcherProvider = dispatcherProvider,
        )
    }

    "with SetMarket event should update state with received market" {
        runTest {
            val market = provideFakeMarket()

            sut.event(MarketDetailContract.Event.SetMarket(market))
            advanceUntilIdle()

            val uiState = sut.state.value
            market shouldBe uiState.market
        }
    }
    "With OnFavoriteClick with an item that is already in favorite we remote it from favorite list" {
        runTest {
            val market = provideFakeMarket(isFavorite = true)

            val expected = market.copy(isFavorite = false)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.OnFavoriteClick(market))
            advanceUntilIdle()

            coVerify(exactly = 1) {
                toggleFavoriteMarketListUseCase.invoke(market.toMarket())
            }

            val uiState = sut.state.value
            expected shouldBe uiState.market
        }
    }
    "With OnFavoriteClick with an item that is not in favorite we add it to favorite list" {
        runTest {
            val market = provideFakeMarket(isFavorite = false)

            val expected = market.copy(isFavorite = true)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.OnFavoriteClick(market))
            advanceUntilIdle()

            coVerify(exactly = 1) {
                toggleFavoriteMarketListUseCase(market.toMarket())
            }

            val uiState = sut.state.value
            expected shouldBe uiState.market
        }
    }
    "get market chart with force refresh is false returns success" {
        runTest {
            val market = provideFakeMarket()
            val chart = provideFakeChart()
            val chartResult = Resource.Success(chart)

            coEvery { getMarketChartUseCase.invoke(any()) } returns flowOf(chartResult)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.GetMarketChart(market.id))
            advanceUntilIdle()

            val uiState = sut.baseState.value
            uiState shouldBeEqual BaseContract.BaseState.OnSuccess
        }
    }
    "get market chart with force refresh is false returns error" {
        runTest {
            val market = provideFakeMarket()
            val chart = provideFakeChart()
            val chartResult = Resource.Error(SocketTimeoutException(), chart)

            coEvery { getMarketChartUseCase.invoke(any()) } returns flowOf(chartResult)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.GetMarketChart(market.id))
            advanceUntilIdle()

            val uiState = sut.baseState.value
            uiState is BaseContract.BaseState.OnError
        }
    }
})

private fun provideFakeMarket(isFavorite: Boolean = false): MarketModel {
    return MarketModel(
        id = UUID.randomUUID().toString(),
        name = "Bitcoin",
        symbol = "BTC",
        imageUrl = "goggle.com",
        currentPrice = Random.nextDouble(1000.0, 5000.0),
        priceChangePercentage24h = Random.nextDouble(1000.0, 5000.0),
        isFavorite = isFavorite,
    )
}

private fun provideFakeChart(): Chart {
    return Chart(persistentListOf())
}