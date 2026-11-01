package ir.composenews.localdatasource.database

import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

actual fun inMemoryMarketDatabase(): MarketDatabase =
    buildMarketDatabase(
        Room.inMemoryDatabaseBuilder<MarketDatabase>(ApplicationProvider.getApplicationContext()),
        PlatformDispatcherProvider(),
        AndroidSQLiteDriver(),
    )

/** Runs the shared `MarketDaoChecks` under Robolectric, which `AndroidSQLiteDriver` needs. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketDaoTest {
    private val db = inMemoryMarketDatabase()
    private val marketDao = db.marketDao()

    @After
    fun tearDown() = db.close()

    @Test
    fun givenInsertedMarket_whenGetMarketList_thenReturnsIt() = runBlocking { marketDao.checkInsertedMarketIsListed() }

    @Test
    fun givenFavorite_whenGetFavoriteMarketList_thenReturnsIt() = runBlocking { marketDao.checkFavoriteIsListed() }

    @Test
    fun givenFavoriteMarket_whenInsertedAgainWithNewData_thenUpdatesDataAndKeepsFavorite() =
        runBlocking { marketDao.checkReinsertUpdatesDataAndKeepsFavorite() }

    @Test
    fun givenMarketsSharingNameAndSymbol_whenInserted_thenBothAreStored() =
        runBlocking { marketDao.checkMarketsSharingNameAndSymbolAreBothStored() }

    @Test
    fun givenRankedAndUnrankedMarkets_whenGetMarketList_thenReturnsRankedOnesByRank() =
        runBlocking { marketDao.checkOnlyRankedMarketsAreListedByRank() }

    @Test
    fun givenFavoriteDroppingOutOfTopMarkets_whenReplaceTopMarkets_thenItIsNotListedButStaysFavorite() =
        runBlocking { marketDao.checkDroppedFavoriteIsUnlistedButStaysFavorite() }
}
