package ir.composenews.marketlist

import io.mockk.coEvery
import io.mockk.mockk
import ir.composenews.core_test.MainDispatcherRule
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class MarketListViewModelTest {
    private val getMarketListUseCase: GetMarketListUseCase = mockk(relaxed = true)
    private val syncMarketListUseCase: SyncMarketListUseCase = mockk(relaxed = true)
    private val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase = mockk(relaxed = true)
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase =
        mockk(relaxed = true)
    private val testScheduler = TestCoroutineScheduler()
    private val dispatcherProvider = TestDispatcherProvider(testScheduler)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(dispatcherProvider)

    private lateinit var sut: MarketListViewModel

    @Before
    fun setup() {
        sut = MarketListViewModel(
            getMarketListUseCase,
            syncMarketListUseCase,
            getFavoriteMarketListUseCase,
            toggleFavoriteMarketListUseCase,
            dispatcherProvider,
        )
    }

    @Test
    fun `with OnSetShowFavoriteList event and showFavoriteList is false then we should hide favorite list`() =
        runTest {
            val expected = false

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(expected))
            advanceUntilIdle()

            val actual = sut.state.value.showFavoriteList

            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `with OnSetShowFavoriteList event and showFavoriteList is true then we should show favorite list`() =
        runTest {
            val expected = true

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(expected))
            advanceUntilIdle()

            val actual = sut.state.value.showFavoriteList

            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `with OnGetMarketList event and showFavorite is false  we should get all market items () `() =
        runTest {
            // Given
            val expectedMarketList = provideMarketList(10)
            coEvery { getMarketListUseCase.invoke() } returns flowOf(expectedMarketList)

            // When
            sut.event(MarketListContract.Event.OnSetShowFavoriteList(false))
            sut.event(MarketListContract.Event.OnGetMarketList)
            advanceUntilIdle()

            // Then
            val actualMarketList = sut.state.value.marketList
            Assert.assertEquals(expectedMarketList, actualMarketList)
        }

    @Test
    fun `with OnGetMarketList event and showFavorite is true  we should get all favorite market items () `() =
        runTest {
            // Given
            val expectedMarketList = provideMarketList(10)
            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(expectedMarketList)

            // When
            sut.event(MarketListContract.Event.OnSetShowFavoriteList(true))
            sut.event(MarketListContract.Event.OnGetMarketList)
            advanceUntilIdle()

            // Then
            val actualMarketList = sut.state.value.marketList
            Assert.assertEquals(expectedMarketList, actualMarketList)
        }

    @Test
    fun `with OnRefresh event and showFavorite is true we should refresh market list with favorites`() =
        runTest {
            val oldMarketList = provideMarketList(2)

            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(oldMarketList)

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(true))
            sut.event(MarketListContract.Event.OnGetMarketList)

            advanceUntilIdle()
            Assert.assertEquals(oldMarketList, sut.state.value.marketList)

            val newMarketList = provideMarketList(4)

            coEvery { getFavoriteMarketListUseCase.invoke() } returns flowOf(newMarketList)

            sut.event(MarketListContract.Event.OnRefresh)

            advanceUntilIdle()
            val actualMarketList = sut.state.value.marketList
            Assert.assertEquals(newMarketList, actualMarketList)
            Assert.assertTrue(!sut.state.value.refreshing)
        }

    @Test
    fun `with OnRefresh event and showFavorite is false we should refresh market list with favorites`() =
        runTest {
            val oldMarketList = provideMarketList(5)

            coEvery { getMarketListUseCase.invoke() } returns flowOf(oldMarketList)

            sut.event(MarketListContract.Event.OnSetShowFavoriteList(false))

            val newMarketList = provideMarketList(5)

            coEvery { getMarketListUseCase.invoke() } returns flowOf(newMarketList)

            sut.event(MarketListContract.Event.OnRefresh)
            advanceUntilIdle()

            val actualMarketList = sut.state.value.marketList
            Assert.assertEquals(newMarketList, actualMarketList)
            Assert.assertTrue(!sut.state.value.refreshing)
        }

    private fun provideMarketList(size: Int): List<Market> {
        return (0 until size).map {
            Market(
                id = UUID.randomUUID().toString(),
                name = "Ethereum$it",
                currentPrice = Random.nextDouble(300.0, 2300.0),
                isFavorite = false,
                imageUrl = "google.com",
            )
        }
    }
}
