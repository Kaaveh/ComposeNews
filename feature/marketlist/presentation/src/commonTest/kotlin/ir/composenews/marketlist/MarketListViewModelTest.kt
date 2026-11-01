@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketlist

import app.cash.turbine.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
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
import ir.composenews.testing.repository.FakeMarketRepository
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class MarketListViewModelTest : StringSpec({

    val dispatcherProvider = TestDispatcherProvider()
    lateinit var repository: FakeMarketRepository
    lateinit var viewModel: MarketListViewModel

    extensions(MainCoroutineListener(dispatcherProvider.testScheduler))

    beforeEach {
        repository = FakeMarketRepository().apply { marketList = { flowOf(markets) } }
        viewModel = MarketListViewModel(
            GetMarketListUseCase(repository),
            SyncMarketListUseCase(repository),
            ToggleFavoriteMarketListUseCase(repository),
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
        repository.syncCalls shouldBe 1
    }

    "Given the sync is running, When the table is still empty, Then the list stays Loading" {
        val syncResult = CompletableDeferred<Resource<Unit, Errors>>()
        repository.marketList = { flowOf(emptyList()) }
        repository.sync = { syncResult.await() }

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
        repository.marketList = { flowOf(emptyList()) }
        repository.sync = { Resource.Error(Errors.ApiError("Internal Server Error", 500)) }

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList shouldBe LoadableData.Error(Errors.ApiError("Internal Server Error", 500))
    }

    "Given stored markets, When the sync fails, Then the stored markets stay on screen" {
        repository.sync = { Resource.Error(Errors.ExceptionError("offline")) }

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList.data?.size shouldBe 3
    }

    "Given a 429, When the sync answers, Then retryInSeconds counts down and blocks syncing until 0" {
        repository.sync = { Resource.Error(Errors.RateLimited(retryAfterSeconds = 3)) }

        viewModel.event(Event.OnGetMarketList)
        viewModel.state.value.retryInSeconds shouldBe 3

        dispatcherProvider.testScheduler.advanceTimeBy(1_000)
        dispatcherProvider.testScheduler.runCurrent()
        viewModel.state.value.retryInSeconds shouldBe 2

        viewModel.event(Event.OnRefresh)
        repository.syncCalls shouldBe 1

        dispatcherProvider.testScheduler.advanceTimeBy(2_000)
        dispatcherProvider.testScheduler.runCurrent()
        viewModel.state.value.retryInSeconds shouldBe 0

        repository.sync = { Resource.Success(Unit) }
        viewModel.event(Event.OnRefresh)
        repository.syncCalls shouldBe 2
    }

    "Given the screen comes back, When OnGetMarketList fires again, Then it doesn't sync again but OnRefresh does" {
        viewModel.event(Event.OnGetMarketList)
        viewModel.event(Event.OnGetMarketList)
        repository.syncCalls shouldBe 1

        viewModel.event(Event.OnRefresh)
        repository.syncCalls shouldBe 2
    }

    "Given Room hasn't re-emitted after the sync wrote, When the sync succeeds, Then the list shows the written markets, not an empty one" {
        var calls = 0
        repository.marketList = { if (calls++ == 0) MutableStateFlow(emptyList()) else flowOf(markets) }

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.marketList.data?.size shouldBe 3
    }

    "Given stored markets, When a refresh fails, Then syncError is set until the next sync starts" {
        viewModel.event(Event.OnGetMarketList)
        repository.sync = { Resource.Error(Errors.ExceptionError("offline")) }

        viewModel.event(Event.OnRefresh)
        viewModel.state.value.syncError shouldBe Errors.ExceptionError("offline")

        repository.sync = { Resource.Success(Unit) }
        viewModel.event(Event.OnRefresh)
        viewModel.state.value.syncError shouldBe null
    }

    "Given a shown sync error, When the UI reports it, Then syncError is cleared" {
        viewModel.event(Event.OnGetMarketList)
        repository.sync = { Resource.Error(Errors.ExceptionError("offline")) }
        viewModel.event(Event.OnRefresh)

        viewModel.event(Event.OnSyncErrorShown)

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
        repository.marketList = { table }
        viewModel.event(Event.OnGetMarketList)
        viewModel.event(Event.OnFilterSelect(Filter.Gainers))

        table.value = markets.map { if (it.id == "eth") it.copy(priceChangePercentage24h = 9.0) else it }

        viewModel.state.value.marketList.data?.map { it.id } shouldBe listOf("eth", "sol", "btc")
        viewModel.state.value.topMover?.id shouldBe "eth"
    }

    "Given every market fell, When loaded, Then there is no top mover" {
        repository.marketList = { flowOf(markets.map { it.copy(priceChangePercentage24h = -1.0) }) }

        viewModel.event(Event.OnGetMarketList)

        viewModel.state.value.topMover shouldBe null
    }

    "Given a market, When favorite click event is triggered, Then toggle favorite use case is called" {
        val marketModel = markets.first().toMarketModel()

        viewModel.event(Event.OnFavoriteClick(market = marketModel))

        repository.toggled shouldBe listOf(marketModel.toMarket())
    }
})

private val markets = listOf(
    Market("btc", "Bitcoin", "BTC", 50000.0, 1.0, "", marketCapRank = 1, totalVolume = 30.0),
    Market("eth", "Ethereum", "ETH", 3000.0, -2.0, "", marketCapRank = 2, totalVolume = 40.0),
    Market("sol", "Solana", "SOL", 150.0, 5.0, "", marketCapRank = 3, totalVolume = 10.0),
)
