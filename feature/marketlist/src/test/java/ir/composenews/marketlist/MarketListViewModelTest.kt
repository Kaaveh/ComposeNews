@file:Suppress("TooGenericExceptionThrown", "MaxLineLength", "ktlint")

package ir.composenews.marketlist

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
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import java.util.UUID
import kotlin.random.Random

class MarketListViewModelTest : StringSpec({

    val getPagedMarketListUseCase: GetPagedMarketListUseCase = mockk(relaxed = true)
    val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase = mockk(relaxed = true)
    val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase = mockk(relaxed = true)
    val testScheduler = TestCoroutineScheduler()
    val dispatcherProvider = TestDispatcherProvider(testScheduler)
    lateinit var viewModel: MarketListViewModel

    extensions(MainCoroutineListener())

    beforeEach {
        viewModel = MarketListViewModel(
            getPagedMarketListUseCase,
            getFavoriteMarketListUseCase,
            toggleFavoriteMarketListUseCase,
            dispatcherProvider,
        )
    }

    "Given initial state, When ViewModel is created, Then state should be default" {
        val initialState = viewModel.state.value

        initialState.favoriteMarketList.shouldBeInstanceOf<LoadableData.Initial>()
        initialState.showFavoriteList shouldBe false
    }

    "Given ViewModel state, When show favorite list is toggled, Then state updates correctly" {
        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))

        viewModel.state.value.showFavoriteList shouldBe true
    }

    "Given favorite list mode is enabled, When favorite market list is fetched, Then loaded state contains favorite markets" {
        val favoriteMarketList = provideMarketList(1, isFavorite = true)
        coEvery { getFavoriteMarketListUseCase() } returns flowOf(favoriteMarketList)

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))
        viewModel.event(MarketListContract.Event.OnGetMarketList)

        val loadedState = viewModel.state.value.favoriteMarketList
        loadedState.shouldBeInstanceOf<LoadableData.Loaded<PersistentList<MarketModel>>>()
        loadedState.data.size shouldBe 1
        loadedState.data.first().isFavorite shouldBe true
    }

    "Given a market, When favorite click event is triggered, Then toggle favorite use case is called" {
        val marketModel = provideMarketList(size = 1).first().toMarketModel()
        coEvery { toggleFavoriteMarketListUseCase(any()) } returns Unit

        viewModel.event(MarketListContract.Event.OnFavoriteClick(market = marketModel))

        coVerify { toggleFavoriteMarketListUseCase(marketModel.toMarket()) }
    }

    "Given favorite list is enabled, When favorites are empty, Then loaded state has empty list" {
        coEvery { getFavoriteMarketListUseCase() } returns flowOf(emptyList())

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))
        viewModel.event(MarketListContract.Event.OnGetMarketList)

        val loadedState = viewModel.state.value.favoriteMarketList
        loadedState.shouldBeInstanceOf<LoadableData.Loaded<PersistentList<MarketModel>>>()
        loadedState.data.isEmpty() shouldBe true
    }

    "Given favorite list is fetching, When state is observed, Then Loading is emitted before Loaded" {
        coEvery { getFavoriteMarketListUseCase() } returns flowOf(provideMarketList(1))

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))

        viewModel.state.test {
            awaitItem() // current state

            viewModel.event(MarketListContract.Event.OnGetMarketList)

            val loadingState = awaitItem()
            loadingState.favoriteMarketList.shouldBeInstanceOf<LoadableData.Loading>()
            cancelAndConsumeRemainingEvents()
        }
    }

    "Given favorites are already loaded, When refresh event fires, Then state transitions through Loading to Loaded again" {
        val marketList = provideMarketList(2)
        coEvery { getFavoriteMarketListUseCase() } returns flowOf(marketList)

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))

        viewModel.state.test {
            awaitItem() // current state

            viewModel.event(MarketListContract.Event.OnGetMarketList)
            awaitItem() // Loading
            awaitItem() // Loaded (first load)

            viewModel.event(MarketListContract.Event.OnGetMarketList)
            val refreshLoadingState = awaitItem().favoriteMarketList
            refreshLoadingState.shouldBeInstanceOf<LoadableData.Loading>()

            val refreshLoadedState = awaitItem().favoriteMarketList
            refreshLoadedState shouldBe LoadableData.Loaded(data = marketList.map { it.toMarketModel() })
            cancelAndConsumeRemainingEvents()
        }
    }

    "Given a favorite list use case with multiple items, When loaded, Then all markets are present and count matches" {
        val count = 5
        val marketList = provideMarketList(count)
        coEvery { getFavoriteMarketListUseCase() } returns flowOf(marketList)

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))
        viewModel.event(MarketListContract.Event.OnGetMarketList)

        val loadedState = viewModel.state.value.favoriteMarketList
        loadedState.shouldBeInstanceOf<LoadableData.Loaded<PersistentList<MarketModel>>>()
        loadedState.data.size shouldBe count
    }

    "Given markets tab is active, When toggled to favorites and back to markets, Then showFavoriteList reflects each toggle" {
        viewModel.state.value.showFavoriteList shouldBe false

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = true))
        viewModel.state.value.showFavoriteList shouldBe true

        viewModel.event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = false))
        viewModel.state.value.showFavoriteList shouldBe false
    }
})

private fun provideMarketList(size: Int, isFavorite: Boolean = false): List<Market> =
    (0 until size).map {
        Market(
            id = UUID.randomUUID().toString(),
            name = "Ethereum$it",
            symbol = "XRP",
            currentPrice = Random.nextDouble(300.0, 2300.0),
            priceChangePercentage24h = Random.nextDouble(300.0, 2300.0),
            isFavorite = isFavorite,
            imageUrl = "google.com",
        )
    }
