package ir.composenews.marketdetail

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.composenews.base.BaseContract
import ir.composenews.core_test.MainDispatcherRule
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.Resource
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class MarketDetailViewModelTest {

    private val getMarketChartUseCase: GetMarketChartUseCase = mockk(relaxed = true)
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase =
        mockk(relaxed = true)
    private val testScheduler = TestCoroutineScheduler()
    private val dispatcherProvider = TestDispatcherProvider(testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(dispatcherProvider)

    private lateinit var sut: MarketDetailViewModel

    @Before
    fun setup() {
        sut = MarketDetailViewModel(
            getMarketChartUseCase = getMarketChartUseCase,
            toggleFavoriteMarketListUseCase = toggleFavoriteMarketListUseCase,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `with SetMarket event should update state with received market`() = runTest {
        val market = provideFakeMarket()

        sut.event(MarketDetailContract.Event.SetMarket(market))
        advanceUntilIdle()

        val uiState = sut.state.value
        assertEquals(market, uiState.market)
    }

    @Test
    fun `With OnFavoriteClick with an item that is already in favorite we remote it from favorite list`() =
        runTest {
            val market = provideFakeMarket(isFavorite = true)

            val expected = market.copy(isFavorite = false)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.OnFavoriteClick(market))
            advanceUntilIdle()

            coVerify(exactly = 1) {
                toggleFavoriteMarketListUseCase.invoke(market)
            }

            val uiState = sut.state.value
            assertEquals(expected, uiState.market)
        }

    @Test
    fun `With OnFavoriteClick with an item that is not in favorite we add it to favorite list`() =
        runTest {
            val market = provideFakeMarket(isFavorite = false)

            val expected = market.copy(isFavorite = true)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.OnFavoriteClick(market))
            advanceUntilIdle()

            coVerify(exactly = 1) {
                toggleFavoriteMarketListUseCase.invoke(market)
            }

            val uiState = sut.state.value
            assertEquals(expected, uiState.market)
        }

    @Test
    fun `get market chart with force refresh is false returns success`() =
        runTest {
            val market = provideFakeMarket()
            val chart = provideFakeChart()
            val chartResult = Resource.Success(chart)

            coEvery { getMarketChartUseCase.invoke(any()) } returns flowOf(chartResult)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.GetMarketChart(market.id))
            advanceUntilIdle()

            val uiState = sut.baseState.value
            assertTrue(uiState is BaseContract.BaseState.OnSuccess)
        }

    @Test
    fun `get market chart with force refresh is false returns error`() =
        runTest {
            val market = provideFakeMarket()
            val chart = provideFakeChart()
            val chartResult = Resource.Error(SocketTimeoutException(), chart)

            coEvery { getMarketChartUseCase.invoke(any()) } returns flowOf(chartResult)

            sut.event(MarketDetailContract.Event.SetMarket(market))
            sut.event(MarketDetailContract.Event.GetMarketChart(market.id))
            advanceUntilIdle()

            val uiState = sut.baseState.value
            assertTrue(uiState is BaseContract.BaseState.OnError)
        }

    private fun provideFakeChart(): Chart {
        return Chart(emptyList())
    }

    private fun provideFakeMarket(isFavorite: Boolean = false): Market {
        return Market(
            id = UUID.randomUUID().toString(),
            name = "Bitcoin",
            imageUrl = "goggle.com",
            currentPrice = Random.nextDouble(1000.0, 5000.0),
            isFavorite = isFavorite,
        )
    }
}
