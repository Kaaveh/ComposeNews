@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketdetail

import app.cash.turbine.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.composenews.base.LoadableData
import ir.composenews.core_test.MainCoroutineListener
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import java.util.UUID

class MarketDetailViewModelTest : StringSpec({
    val getMarketChartUseCase: GetMarketChartUseCase = mockk(relaxed = true)
    val getMarketDetailUseCase: GetMarketDetailUseCase = mockk(relaxed = true)
    val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase = mockk(relaxed = true)
    val testScheduler = TestCoroutineScheduler()
    val dispatcherProvider = TestDispatcherProvider(testScheduler)
    lateinit var viewModel: MarketDetailViewModel

    extensions(MainCoroutineListener())

    beforeEach {
        viewModel = MarketDetailViewModel(
            getMarketChartUseCase = getMarketChartUseCase,
            getMarketDetailUseCase = getMarketDetailUseCase,
            toggleFavoriteMarketListUseCase = toggleFavoriteMarketListUseCase,
            dispatcherProvider = dispatcherProvider,
        )
    }

    "Given initial state, When ViewModel is created, Then state should be initial" {
        val initialState = viewModel.state.value

        initialState.market.shouldBeInstanceOf<LoadableData.Initial>()
        initialState.marketDetail.shouldBeInstanceOf<LoadableData.Initial>()
        initialState.marketChart.shouldBeInstanceOf<LoadableData.Initial>()
    }

    "Given market model from nav stack, When set market event is triggered, Then market state is updated" {
        val marketModel = provideMarketList(1).first().toMarketModel()

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        marketState.data shouldBe marketModel
    }

    "Given calling market detail use case, When get market detail is triggered, Then loading and loaded states are set" {
        val marketDetail = provideMarketDetail()
        coEvery { getMarketDetailUseCase(id = any()) } returns flowOf(Resource.Success(marketDetail))

        viewModel.state.test {
            val initState = awaitItem().marketDetail
            initState.shouldBeInstanceOf<LoadableData.Initial>()

            viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "testId"))

            val loadingState = awaitItem().marketDetail
            loadingState.shouldBeInstanceOf<LoadableData.Loading>()

            val loadedState = awaitItem().marketDetail
            loadedState shouldBe LoadableData.Loaded(data = marketDetail)
        }
    }

    "Given market chart use case, When get market chart is triggered, Then loading and loaded states are set" {
        val marketChart = provideMarketChart()
        coEvery { getMarketChartUseCase(id = any()) } returns flowOf(Resource.Success(marketChart))

        viewModel.state.test {
            val initState = awaitItem().marketChart
            initState.shouldBeInstanceOf<LoadableData.Initial>()

            viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "testId"))

            val loadingState = awaitItem().marketChart
            loadingState.shouldBeInstanceOf<LoadableData.Loading>()

            val loadedState = awaitItem().marketChart
            loadedState shouldBe LoadableData.Loaded(data = marketChart)
        }
    }

    "Given market detail use case fails, When get market detail is triggered, Then error state is set" {
        val exception = RuntimeException("Network Error")
        coEvery { getMarketDetailUseCase(id = any()) } returns flow { throw exception }

        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "testId"))

        val state = viewModel.state.value.marketDetail
        state.shouldBeInstanceOf<LoadableData.Error>()
        (state.error as Errors.ExceptionError).message shouldBe exception.message
    }

    "Given market chart use case fails, When get market chart is triggered, Then error state is set" {
        val exception = RuntimeException("Network Error")
        coEvery { getMarketChartUseCase(id = any()) } returns flow { throw exception }

        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "testId"))

        val state = viewModel.state.value.marketChart
        state.shouldBeInstanceOf<LoadableData.Error>()
        (state.error as Errors.ExceptionError).message shouldBe exception.message
    }

    "Given a market, When favorite click event is triggered, Then toggle favorite use case is called" {
        val marketModel = provideMarketList(1).first().toMarketModel()
        coEvery { toggleFavoriteMarketListUseCase(any()) } returns Unit

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))
        viewModel.event(MarketDetailContract.Event.OnFavoriteClick(market = marketModel))

        coVerify { toggleFavoriteMarketListUseCase(marketModel.toMarket()) }
    }
})

private fun provideMarketList(size: Int, isFavorite: Boolean = false): List<Market> =
    (0 until size).map {
        Market(
            id = UUID.randomUUID().toString(),
            name = "Market$it",
            symbol = "SYM",
            currentPrice = 100.0,
            priceChangePercentage24h = 1.0,
            isFavorite = isFavorite,
            imageUrl = "google.com",
        )
    }

private fun provideMarketDetail(): MarketDetail = MarketDetail(
    id = UUID.randomUUID().toString(),
    name = "Test Market",
    marketCapRank = 1,
    marketData = null,
)

private fun provideMarketChart(): MarketChart = MarketChart(
    prices = persistentListOf(
        Pair(System.currentTimeMillis(), 100.0),
        Pair(System.currentTimeMillis(), 101.0),
    ),
)
