@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketdetail

import app.cash.turbine.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ir.composenews.base.LoadableData
import ir.composenews.testing.MainCoroutineListener
import ir.composenews.testing.dispatcher.TestDispatcherProvider
import ir.composenews.testing.repository.FakeMarketRepository
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.PricePoint
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MarketDetailViewModelTest : StringSpec({
    val dispatcherProvider = TestDispatcherProvider()
    lateinit var repository: FakeMarketRepository
    lateinit var viewModel: MarketDetailViewModel

    extensions(MainCoroutineListener(dispatcherProvider.testScheduler))

    beforeEach {
        // Like the relaxed mocks these replace: no stored market, and requests that complete without a value.
        repository = FakeMarketRepository().apply {
            marketById = { flowOf(null) }
            chart = { _, _ -> emptyFlow() }
            detail = { emptyFlow() }
        }
        viewModel = MarketDetailViewModel(
            getMarketChartUseCase = GetMarketChartUseCase(repository),
            getMarketDetailUseCase = GetMarketDetailUseCase(repository),
            toggleFavoriteMarketListUseCase = ToggleFavoriteMarketListUseCase(repository),
            getMarketByIdUseCase = GetMarketByIdUseCase(repository),
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
        repository.detail = { flowOf(Resource.Success(marketDetail)) }

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
        repository.chart = { _, _ -> flowOf(Resource.Success(marketChart)) }

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
        repository.detail = { flow { throw exception } }

        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "testId"))

        val state = viewModel.state.value.marketDetail
        state.shouldBeInstanceOf<LoadableData.Error>()
        (state.error as Errors.ExceptionError).message shouldBe exception.message
    }

    "Given market chart use case fails, When get market chart is triggered, Then error state is set" {
        val exception = RuntimeException("Network Error")
        repository.chart = { _, _ -> flow { throw exception } }

        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "testId"))

        val state = viewModel.state.value.marketChart
        state.shouldBeInstanceOf<LoadableData.Error>()
        (state.error as Errors.ExceptionError).message shouldBe exception.message
    }

    "Given a market, When favorite click event is triggered, Then toggle favorite use case is called" {
        val marketModel = provideMarketList(1).first().toMarketModel()

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))
        viewModel.event(MarketDetailContract.Event.OnFavoriteClick(market = marketModel))

        repository.toggled shouldBe listOf(marketModel.toMarket())
    }

    "Given a market that is already favorited, When set market event fires, Then market state has isFavorite true" {
        val marketModel = provideMarketList(1, isFavorite = true).first().toMarketModel()

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        marketState.data.isFavorite shouldBe true
    }

    "Given DB emits a favorited market, When set market fires, Then isFavorite reflects DB value" {
        val marketModel = provideMarketList(1, isFavorite = false).first().toMarketModel()
        val dbMarket = marketModel.toMarket().copy(isFavorite = true)
        repository.marketById = { id -> flowOf(dbMarket.takeIf { id == marketModel.id }) }

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        marketState.data.isFavorite shouldBe true
    }

    "Given DB emits a non-favorited market, When set market fires, Then isFavorite reflects DB value" {
        val marketModel = provideMarketList(1, isFavorite = true).first().toMarketModel()
        val dbMarket = marketModel.toMarket().copy(isFavorite = false)
        repository.marketById = { id -> flowOf(dbMarket.takeIf { id == marketModel.id }) }

        viewModel.event(MarketDetailContract.Event.SetMarket(market = marketModel))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        marketState.data.isFavorite shouldBe false
    }

    "Given a market with positive price change, When set market event fires, Then priceChangePercentage24h is positive in state" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 7.5,
            imageUrl = "",
            isFavorite = false,
        )

        viewModel.event(MarketDetailContract.Event.SetMarket(market = market.toMarketModel()))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        (marketState.data.priceChangePercentage24h > 0) shouldBe true
    }

    "Given a market with negative price change, When set market event fires, Then priceChangePercentage24h is negative in state" {
        val market = Market(
            id = "eth",
            name = "Ethereum",
            symbol = "ETH",
            currentPrice = 3000.0,
            priceChangePercentage24h = -3.2,
            imageUrl = "",
            isFavorite = false,
        )

        viewModel.event(MarketDetailContract.Event.SetMarket(market = market.toMarketModel()))

        val marketState = viewModel.state.value.market
        marketState.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        (marketState.data.priceChangePercentage24h < 0) shouldBe true
    }

    "Given market chart with multiple data points, When loaded, Then all data points are present in state" {
        val prices = persistentListOf(
            Pair(1000L, 100.0),
            Pair(2000L, 110.0),
            Pair(3000L, 105.0),
        )
        val marketChart = MarketChart(prices = prices)
        repository.chart = { _, _ -> flowOf(Resource.Success(marketChart)) }

        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "btc"))

        val chartState = viewModel.state.value.marketChart
        chartState.shouldBeInstanceOf<LoadableData.Loaded<MarketChart>>()
        chartState.data.prices.size shouldBe 3
    }

    "Given chart and detail fetch together, When both complete, Then both states are Loaded" {
        repository.chart = { _, _ -> flowOf(Resource.Success(provideMarketChart())) }
        repository.detail = { flowOf(Resource.Success(provideMarketDetail())) }

        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "btc"))
        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "btc"))

        viewModel.state.value.marketChart.shouldBeInstanceOf<LoadableData.Loaded<MarketChart>>()
        viewModel.state.value.marketDetail.shouldBeInstanceOf<LoadableData.Loaded<MarketDetail>>()
    }

    "Given market detail API returns Resource.Error, When event triggered, Then error state contains the API error" {
        val apiError = Errors.ApiError(message = "Not Found", code = 404)
        repository.detail = { flowOf(Resource.Error(apiError)) }

        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "unknown"))

        val state = viewModel.state.value.marketDetail
        state.shouldBeInstanceOf<LoadableData.Error>()
        (state.error as Errors.ApiError).code shouldBe 404
    }

    "Given a shown market, When a chart range is selected, Then the chart reloads for that many days" {
        val weekChart = MarketChart(persistentListOf(PricePoint(1L, 100.0), PricePoint(2L, 110.0)))
        repository.chart = { id, days -> if (id == "btc" && days == 7) flowOf(Resource.Success(weekChart)) else emptyFlow() }
        viewModel.event(MarketDetailContract.Event.SetMarket(provideMarketList(1).first().toMarketModel().copy(id = "btc")))

        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Week))

        viewModel.state.value.chartRange shouldBe MarketDetailContract.ChartRange.Week
        viewModel.state.value.marketChart shouldBe LoadableData.Loaded(weekChart)
        viewModel.state.value.rangeChange shouldBe 10.0
    }

    "Given the selected range, When it is selected again, Then the chart is not reloaded" {
        viewModel.event(MarketDetailContract.Event.SetMarket(provideMarketList(1).first().toMarketModel().copy(id = "same")))

        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Day))

        repository.chartCalls.none { it.first == "same" } shouldBe true
    }

    "Given a slow chart for the old range, When it answers after a new range was picked, Then it is ignored" {
        val dayChart = MutableSharedFlow<Resource<MarketChart, Errors>>()
        val weekChart = MarketChart(persistentListOf(PricePoint(1L, 100.0), PricePoint(2L, 90.0)))
        repository.chart = { id, days ->
            when {
                id != "slow" -> emptyFlow()
                days == 7 -> flowOf(Resource.Success(weekChart))
                else -> dayChart
            }
        }
        viewModel.event(MarketDetailContract.Event.SetMarket(provideMarketList(1).first().toMarketModel().copy(id = "slow")))
        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "slow"))

        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Week))
        dayChart.emit(Resource.Success(provideMarketChart()))

        viewModel.state.value.marketChart shouldBe LoadableData.Loaded(weekChart)
        viewModel.state.value.rangeChange shouldBe -10.0
    }

    "Given a range picked for one market, When another market is set, Then the range goes back to 1D" {
        val model = provideMarketList(1).first().toMarketModel()
        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "a")))
        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Year))

        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "a")))
        viewModel.state.value.chartRange shouldBe MarketDetailContract.ChartRange.Year

        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "b")))
        viewModel.state.value.chartRange shouldBe MarketDetailContract.ChartRange.Day
    }

    "Given one market's chart and detail, When another market is set, Then they are cleared until it loads its own" {
        val model = provideMarketList(1).first().toMarketModel()
        repository.chart = { id, days -> if (id == "a" && days == 1) flowOf(Resource.Success(provideMarketChart())) else emptyFlow() }
        repository.detail = { id -> if (id == "a") flowOf(Resource.Success(provideMarketDetail())) else emptyFlow() }
        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "a")))
        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "a"))
        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "a"))

        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "a")))
        viewModel.state.value.marketChart.shouldBeInstanceOf<LoadableData.Loaded<MarketChart>>()

        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "b")))
        viewModel.state.value.marketChart shouldBe LoadableData.Initial
        viewModel.state.value.marketDetail shouldBe LoadableData.Initial
        viewModel.state.value.rangeChange shouldBe null
    }

    "Given a chart requested before the market is set, When a range is selected, Then that chart reloads for the new range" {
        repository.chart = { id, days -> if (id == "early" && days == 30) flowOf(Resource.Success(provideMarketChart())) else emptyFlow() }
        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "early"))

        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Month))

        repository.chartCalls.count { it == ("early" to 30) } shouldBe 1
    }

    "Given a week chart requested for the next market before it is set, When it is set, Then the range stays a week" {
        val model = provideMarketList(1).first().toMarketModel()
        repository.chart = { _, _ -> flowOf(Resource.Success(provideMarketChart())) }
        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "a")))
        viewModel.event(MarketDetailContract.Event.OnChartRangeSelect(MarketDetailContract.ChartRange.Week))
        viewModel.event(MarketDetailContract.Event.GetMarketChart(marketId = "b"))

        viewModel.event(MarketDetailContract.Event.SetMarket(model.copy(id = "b")))

        repository.chartCalls.count { it == ("b" to 7) } shouldBe 1
        viewModel.state.value.chartRange shouldBe MarketDetailContract.ChartRange.Week
        viewModel.state.value.marketChart.shouldBeInstanceOf<LoadableData.Loaded<MarketChart>>()
    }

    "Given a slow detail for the previous market, When it answers after the next one was requested, Then it is ignored" {
        val slowDetail = MutableSharedFlow<Resource<MarketDetail, Errors>>()
        val nextDetail = provideMarketDetail()
        repository.detail = { id ->
            when (id) {
                "btc" -> slowDetail
                "eth" -> flowOf(Resource.Success(nextDetail))
                else -> emptyFlow()
            }
        }
        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "btc"))

        viewModel.event(MarketDetailContract.Event.GetMarketDetail(marketId = "eth"))
        slowDetail.emit(Resource.Success(provideMarketDetail()))

        viewModel.state.value.marketDetail shouldBe LoadableData.Loaded(nextDetail)
    }
})

private var nextId = 0

private fun provideMarketList(size: Int, isFavorite: Boolean = false): List<Market> =
    (0 until size).map {
        Market(
            id = "market-${nextId++}",
            name = "Market$it",
            symbol = "SYM",
            currentPrice = 100.0,
            priceChangePercentage24h = 1.0,
            isFavorite = isFavorite,
            imageUrl = "google.com",
        )
    }

private fun provideMarketDetail(): MarketDetail = MarketDetail(
    id = "detail-${nextId++}",
    name = "Test Market",
    marketCapRank = 1,
    marketData = null,
)

private fun provideMarketChart(): MarketChart = MarketChart(
    prices = persistentListOf(
        PricePoint(CHART_TIME, 100.0),
        PricePoint(CHART_TIME, 101.0),
    ),
)

private const val CHART_TIME = 1_700_000_000_000L
