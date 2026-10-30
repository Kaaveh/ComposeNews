@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketlist

import app.cash.turbine.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import ir.composenews.base.LoadableData
import ir.composenews.domain.model.Market
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketlist.MarketListContract.Event
import ir.composenews.marketlist.MarketListContract.Filter
import ir.composenews.marketlist.MarketListContract.Header
import ir.composenews.marketlist.MarketListContract.Sort
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.testing.MainCoroutineListener
import ir.composenews.testing.dispatcher.TestDispatcherProvider
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class MarketListViewModelTest : StringSpec({

    val dispatcherProvider = TestDispatcherProvider()
    lateinit var getMarketListUseCase: GetMarketListUseCase
    lateinit var syncMarketListUseCase: SyncMarketListUseCase
    lateinit var toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase
    lateinit var viewModel: MarketListViewModel

    extensions(MainCoroutineListener(dispatcherProvider.testScheduler))

    beforeEach {
        getMarketListUseCase = mockk()
        syncMarketListUseCase = mockk()
        toggleFavoriteMarketListUseCase = mockk(relaxed = true)
        every { getMarketListUseCase() } returns flowOf(markets)
        coEvery { syncMarketListUseCase() } returns Resource.Success(Unit)
        viewModel = MarketListViewModel(
            getMarketListUseCase,
            syncMarketListUseCase,
            toggleFavoriteMarketListUseCase,
            dispatcherProvider,
        )
    }

    "Given initial state, When ViewModel is created, Then state should be default" {
        val state = viewModel.state.value

        state.marketList.shouldBeInstanceOf<LoadableData.Initial>()
        state.filter shouldBe Filter.All
        state.sort shouldBe Sort.MarketCap
        state.header shouldBe Header.ByMarketCap
        state.retryInSeconds shouldBe 0
    }

    "Given stored markets, When OnGetMarketList fires, Then it syncs and shows them by market cap with the top mover" {
        viewModel.event(Event.OnGetMarketList)

        val state = viewModel.state.value
        state.marketList.data?.map { it.id } shouldBe listOf("btc", "eth", "sol")
        state.topMover?.id shouldBe "sol"
        state.isRefreshing shouldBe false
        coVerify(exactly = 1) { syncMarketListUseCase() }
    }

    "Given the sync is running, When the table is still empty, Then the list stays Loading" {
        val syncResult = CompletableDeferred<Resource<Unit, Errors>>()
        every { getMarketListUseCase() } returns flowOf(emptyList())
        coEvery { syncMarketListUseCase() } coAnswers { syncResult.await() }

        viewModel.state.test {
            awaitItem().marketList.shouldBeInstanceOf<LoadableData.Initial>()
            viewModel.event(Event.OnGetMarketList)
            awaitItem().run {
                marketList.shouldBeInstanceOf<LoadableData.Loading>()
                isRefreshing shouldBe true
            }
            cancelAndIgnoreRemainingEvents()
        }
        syncResult.complete(Resource.Success(Unit))

        viewModel.state.value.isRefreshing shouldBe false
        viewModel.state.value.marketList.shouldBeInstanceOf<LoadableData.Loaded<*>>()
        viewModel.state.value.marketList.data shouldBe emptyList()
    }

    "Given no stored markets, When the sync fails, Then the list shows the error" {
        every { getMarketListUseCase() } returns flowOf(emptyList())
        coEvery { syncMarketListUseCase() } returns Resource.Error(Errors.ApiError("Internal Server Error", 500))

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList shouldBe LoadableData.Error(Errors.ApiError("Internal Server Error", 500))
    }

    "Given stored markets, When the sync fails, Then the stored markets stay on screen" {
        coEvery { syncMarketListUseCase() } returns Resource.Error(Errors.ExceptionError("offline"))

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList.data?.size shouldBe 3
    }

    "Given a 429, When the sync answers, Then retryInSeconds counts down and blocks syncing until 0" {
        coEvery { syncMarketListUseCase() } returns Resource.Error(Errors.RateLimited(retryAfterSeconds = 3))

        viewModel.event(Event.OnGetMarketList)
        viewModel.state.value.retryInSeconds shouldBe 3

        dispatcherProvider.testScheduler.advanceTimeBy(1_000)
        dispatcherProvider.testScheduler.runCurrent()
        viewModel.state.value.retryInSeconds shouldBe 2

        viewModel.event(Event.OnRefresh)
        coVerify(exactly = 1) { syncMarketListUseCase() }

        dispatcherProvider.testScheduler.advanceTimeBy(2_000)
        dispatcherProvider.testScheduler.runCurrent()
        viewModel.state.value.retryInSeconds shouldBe 0

        coEvery { syncMarketListUseCase() } returns Resource.Success(Unit)
        viewModel.event(Event.OnRefresh)
        coVerify(exactly = 2) { syncMarketListUseCase() }
    }

    "Given the screen comes back, When OnGetMarketList fires again, Then it doesn't sync again but OnRefresh does" {
        viewModel.event(Event.OnGetMarketList)
        viewModel.event(Event.OnGetMarketList)
        coVerify(exactly = 1) { syncMarketListUseCase() }

        viewModel.event(Event.OnRefresh)
        coVerify(exactly = 2) { syncMarketListUseCase() }
    }

    "Given Room hasn't re-emitted after the sync wrote, When the sync succeeds, Then the list shows the written markets, not an empty one" {
        every { getMarketListUseCase() } returnsMany listOf(MutableStateFlow(emptyList()), flowOf(markets))

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList.data?.size shouldBe 3
    }

    "Given stored markets, When a refresh fails, Then syncError is set until the next sync starts" {
        viewModel.event(Event.OnGetMarketList)
        coEvery { syncMarketListUseCase() } returns Resource.Error(Errors.ExceptionError("offline"))

        viewModel.event(Event.OnRefresh)
        viewModel.state.value.syncError shouldBe Errors.ExceptionError("offline")

        coEvery { syncMarketListUseCase() } returns Resource.Success(Unit)
        viewModel.event(Event.OnRefresh)
        viewModel.state.value.syncError shouldBe null
    }

    "Given loaded markets, When Gainers is selected, Then only gainers show, highest change first" {
        viewModel.event(Event.OnGetMarketList)

        viewModel.event(Event.OnFilterSelect(Filter.Gainers))

        viewModel.state.value.marketList.data?.map { it.id } shouldBe listOf("sol", "btc")
        viewModel.state.value.header shouldBe Header.Gainers
    }

    "Given loaded markets, When Losers is selected, Then only losers show, lowest change first" {
        viewModel.event(Event.OnGetMarketList)

        viewModel.event(Event.OnFilterSelect(Filter.Losers))

        viewModel.state.value.marketList.data?.map { it.id } shouldBe listOf("eth")
        viewModel.state.value.header shouldBe Header.Losers
    }

    "Given loaded markets, When Volume sort is selected, Then markets sort by volume, highest first" {
        viewModel.event(Event.OnGetMarketList)

        viewModel.event(Event.OnSortSelect(Sort.Volume))

        viewModel.state.value.marketList.data?.map { it.id } shouldBe listOf("eth", "btc", "sol")
        viewModel.state.value.header shouldBe Header.ByVolume
    }

    "Given the table changes, When Room emits again, Then the list keeps the selected filter" {
        val table = MutableStateFlow(markets)
        every { getMarketListUseCase() } returns table
        viewModel.event(Event.OnGetMarketList)
        viewModel.event(Event.OnFilterSelect(Filter.Gainers))

        table.value = markets.map { if (it.id == "eth") it.copy(priceChangePercentage24h = 9.0) else it }

        viewModel.state.value.marketList.data?.map { it.id } shouldBe listOf("eth", "sol", "btc")
        viewModel.state.value.topMover?.id shouldBe "eth"
    }

    "Given every market fell, When loaded, Then there is no top mover" {
        every { getMarketListUseCase() } returns flowOf(markets.map { it.copy(priceChangePercentage24h = -1.0) })

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.topMover shouldBe null
    }

    "Given a market, When favorite click event is triggered, Then toggle favorite use case is called" {
        val marketModel = markets.first().toMarketModel()

        viewModel.event(Event.OnFavoriteClick(market = marketModel))

        coVerify { toggleFavoriteMarketListUseCase(marketModel.toMarket()) }
    }
})

private val markets = listOf(
    Market("btc", "Bitcoin", "BTC", 50000.0, 1.0, "", marketCapRank = 1, totalVolume = 30.0),
    Market("eth", "Ethereum", "ETH", 3000.0, -2.0, "", marketCapRank = 2, totalVolume = 40.0),
    Market("sol", "Solana", "SOL", 150.0, 5.0, "", marketCapRank = 3, totalVolume = 10.0),
)
