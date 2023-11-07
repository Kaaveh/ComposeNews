package ir.composenews.marketlist

import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.mockk
import ir.composenews.core_test.MainCoroutineListener
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.mapper.toMarket
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import java.util.UUID
import kotlin.random.Random

class MarketListViewModelTest : StringSpec({
    val getMarketListUseCase: GetMarketListUseCase = mockk(relaxed = true)
    val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase = mockk(relaxed = true)
    val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase = mockk(relaxed = true)
    val testScheduler = TestCoroutineScheduler()
    val dispatcherProvider = TestDispatcherProvider(testScheduler)
    lateinit var sut: MarketListViewModel

    listeners(MainCoroutineListener())

    beforeSpec {
        sut = MarketListViewModel(
            getMarketListUseCase,
            getFavoriteMarketListUseCase,
            toggleFavoriteMarketListUseCase,
            dispatcherProvider,
        )
    }

    "with OnSetShowFavoriteList event and showFavoriteList is false then we should hide favorite list" {
        runTest {
            val expected = false

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(expected))
            advanceUntilIdle()

            val actual = sut.state.value.showFavoriteList

            Assert.assertEquals(expected, actual)
        }
    }

    "with OnSetShowFavoriteList event and showFavoriteList is true then we should show favorite list" {
        runTest {
            val expected = true

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(expected))
            advanceUntilIdle()

            val actual = sut.state.value.showFavoriteList

            Assert.assertEquals(expected, actual)
        }
    }

    "with OnGetMarketList event and showFavorite is false  we should get all market items ()" {
        runTest {
            // Given
            val expectedMarketList = provideMarketList(10)
            coEvery { getMarketListUseCase.invoke() } returns flowOf(expectedMarketList)

            // When
            sut.event(MarketListContract.Event.OnSetShowFavoriteList(false))
            sut.event(MarketListContract.Event.OnGetMarketList)
            advanceUntilIdle()

            // Then
            val actualMarketList = sut.state.value.marketList.map { it.toMarket() }
            Assert.assertEquals(expectedMarketList, actualMarketList)
        }
    }

    "with OnGetMarketList event and showFavorite is true  we should get all favorite market items ()" {
        runTest {
            // Given
            val expectedMarketList = provideMarketList(10)
            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(expectedMarketList)

            // When
            sut.event(MarketListContract.Event.OnSetShowFavoriteList(true))
            sut.event(MarketListContract.Event.OnGetMarketList)
            advanceUntilIdle()

            // Then
            val actualMarketList = sut.state.value.marketList.map { it.toMarket() }
            Assert.assertEquals(expectedMarketList, actualMarketList)
        }
    }

    "with OnRefresh event and showFavorite is true we should refresh market list with favorites" {
        runTest {
            val oldMarketList = provideMarketList(2)

            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(oldMarketList)

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(true))
            sut.event(MarketListContract.Event.OnGetMarketList)

            advanceUntilIdle()
            Assert.assertEquals(oldMarketList, sut.state.value.marketList.map { it.toMarket() })

            val newMarketList = provideMarketList(4)

            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(newMarketList)

            sut.event(MarketListContract.Event.OnRefresh)

            advanceUntilIdle()
            val actualMarketList = sut.state.value.marketList.map { it.toMarket() }
            Assert.assertEquals(newMarketList, actualMarketList)
            Assert.assertTrue(!sut.state.value.refreshing)
        }
    }

    "with OnRefresh event and showFavorite is false we should refresh market list with favorites" {
        runTest {
            val oldMarketList = provideMarketList(5)

            coEvery { getMarketListUseCase.invoke() } returns flowOf(oldMarketList)

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(false))

            val newMarketList = provideMarketList(5)

            coEvery { getMarketListUseCase.invoke() } returns flowOf(newMarketList)

            sut.event(MarketListContract.Event.OnRefresh)
            advanceUntilIdle()

            val actualMarketList = sut.state.value.marketList.map { it.toMarket() }
            Assert.assertEquals(newMarketList, actualMarketList)
            Assert.assertTrue(!sut.state.value.refreshing)
        }
    }

})

private fun provideMarketList(size: Int): List<Market> {
    return (0 until size).map {
        Market(
            id = UUID.randomUUID().toString(),
            name = "Ethereum$it",
            symbol = "XRP",
            currentPrice = Random.nextDouble(300.0, 2300.0),
            priceChangePercentage24h = Random.nextDouble(300.0, 2300.0),
            isFavorite = false,
            imageUrl = "google.com",
        )
    }
}