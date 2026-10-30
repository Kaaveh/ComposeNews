@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketlist

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import ir.composenews.base.LoadableData
import ir.composenews.domain.model.Market
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketlist.FavoriteListContract.Event
import ir.composenews.marketlist.FavoriteListContract.Sort
import ir.composenews.testing.MainCoroutineListener
import ir.composenews.testing.dispatcher.TestDispatcherProvider
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.coroutines.flow.MutableStateFlow

class FavoriteListViewModelTest : StringSpec({

    val dispatcherProvider = TestDispatcherProvider()
    lateinit var favorites: MutableStateFlow<List<Market>>
    lateinit var toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase
    lateinit var viewModel: FavoriteListViewModel

    extensions(MainCoroutineListener(dispatcherProvider.testScheduler))

    beforeEach {
        favorites = MutableStateFlow(storedFavorites)
        val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase = mockk()
        every { getFavoriteMarketListUseCase() } returns favorites
        toggleFavoriteMarketListUseCase = mockk(relaxed = true)
        viewModel = FavoriteListViewModel(getFavoriteMarketListUseCase, toggleFavoriteMarketListUseCase, dispatcherProvider)
    }

    "Given initial state, When ViewModel is created, Then favorites are Initial, sorted by name, count 0" {
        viewModel.state.value.favoriteList.shouldBeInstanceOf<LoadableData.Initial>()
        viewModel.state.value.sort shouldBe Sort.Name
        viewModel.state.value.count shouldBe 0
    }

    "Given stored favorites, When OnGetFavoriteList fires, Then they load sorted by name with their count" {
        viewModel.event(Event.OnGetFavoriteList)

        viewModel.state.value.favoriteList.data?.map { it.id } shouldBe listOf("btc", "eth", "sol")
        viewModel.state.value.count shouldBe 3
    }

    "Given no favorites, When loaded, Then the list is empty and the count is 0" {
        favorites.value = emptyList()

        viewModel.event(Event.OnGetFavoriteList)

        viewModel.state.value.favoriteList.data shouldBe emptyList()
        viewModel.state.value.count shouldBe 0
    }

    "Given loaded favorites, When Price sort is selected, Then they sort by price, highest first" {
        viewModel.event(Event.OnGetFavoriteList)

        viewModel.event(Event.OnSortSelect(Sort.Price))

        viewModel.state.value.favoriteList.data?.map { it.id } shouldBe listOf("btc", "eth", "sol")
    }

    "Given loaded favorites, When 24h change sort is selected, Then they sort by change, highest first" {
        viewModel.event(Event.OnGetFavoriteList)

        viewModel.event(Event.OnSortSelect(Sort.Change24h))

        viewModel.state.value.favoriteList.data?.map { it.id } shouldBe listOf("sol", "btc", "eth")
    }

    "Given a selected sort, When a favorite is removed, Then the list keeps the sort and the count drops" {
        viewModel.event(Event.OnGetFavoriteList)
        viewModel.event(Event.OnSortSelect(Sort.Change24h))

        favorites.value = storedFavorites.filterNot { it.id == "sol" }

        viewModel.state.value.favoriteList.data?.map { it.id } shouldBe listOf("btc", "eth")
        viewModel.state.value.count shouldBe 2
    }

    "Given a favorite, When favorite click event is triggered, Then toggle favorite use case is called" {
        val market = storedFavorites.first().toMarketModel()

        viewModel.event(Event.OnFavoriteClick(market))

        coVerify { toggleFavoriteMarketListUseCase(market.toMarket()) }
    }
})

// Room returns favorites ordered by name, but "eth" first here checks that the ViewModel sorts them.
private val storedFavorites = listOf(
    Market("eth", "ethereum", "ETH", 3000.0, -2.0, "", isFavorite = true),
    Market("btc", "Bitcoin", "BTC", 50000.0, 1.0, "", isFavorite = true),
    Market("sol", "Solana", "SOL", 150.0, 5.0, "", isFavorite = true),
)
