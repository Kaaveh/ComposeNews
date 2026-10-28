package ir.composenews.localdatasource.database

import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketDaoTest {
    private val db =
        buildMarketDatabase(
            Room.inMemoryDatabaseBuilder<MarketDatabase>(ApplicationProvider.getApplicationContext()),
            PlatformDispatcherProvider(),
            AndroidSQLiteDriver(),
        )
    private val marketDao = db.marketDao()

    @After
    fun tearDown() = db.close()

    @Test
    fun givenInsertedMarket_whenGetMarketList_thenReturnsIt() =
        runBlocking {
            val market = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE)

            marketDao.insertMarkets(listOf(market))

            assertEquals(listOf(market), marketDao.getMarketList().first())
        }

    @Test
    fun givenInsertedFavorite_whenGetFavoriteMarketList_thenReturnsIt() =
        runBlocking {
            val favorite = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE)
            marketDao.insertMarkets(listOf(favorite, MarketEntity("2", "Ethereum", "ETH", 3000.0, 1.0, "url", FALSE)))

            assertEquals(listOf(favorite), marketDao.getFavoriteMarketList().first())
        }

    @Test
    fun givenFavoriteMarket_whenInsertedAgainWithNewData_thenUpdatesDataAndKeepsFavorite() =
        runBlocking {
            val market = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE)
            marketDao.insertMarkets(listOf(market))
            marketDao.updateFavoriteStatus(id = "1", isFavorite = TRUE)

            marketDao.insertMarkets(listOf(market.copy(currentPrice = 50001.0, priceChangePercentage24h = 5.000001)))

            assertEquals(
                listOf(market.copy(currentPrice = 50001.0, priceChangePercentage24h = 5.000001, isFavorite = TRUE)),
                marketDao.getMarketList().first(),
            )
            assertEquals(TRUE, marketDao.getMarketById("1").first()?.isFavorite)
        }

    @Test
    fun givenMarketsSharingNameAndSymbol_whenInserted_thenBothAreStored() =
        runBlocking {
            val markets =
                listOf(
                    MarketEntity("a", "Pepe", "PEPE", 1.0, 1.0, "url", FALSE),
                    MarketEntity("b", "Pepe", "PEPE", 2.0, 2.0, "url", FALSE),
                )

            marketDao.insertMarkets(markets)

            assertEquals(markets, marketDao.getMarketList().first().sortedBy { it.id })
        }
}
