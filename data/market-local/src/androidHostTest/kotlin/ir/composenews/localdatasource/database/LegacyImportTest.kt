package ir.composenews.localdatasource.database

import android.content.Context
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.core.app.ApplicationProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LegacyImportTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val legacy = context.getDatabasePath(LEGACY_DATABASE_NAME)

    @Test
    fun givenLegacySqlDelightDatabase_whenRoomOpens_thenFavoritesAreKeptAndLegacyFileIsDeleted() =
        runBlocking {
            createLegacyDatabase()

            val favorites = openAndGetFavorites()

            assertEquals(listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE)), favorites)
            assertFalse(legacy.exists())
        }

    @Test
    fun givenMarketSyncedBeforeImport_whenRoomOpens_thenLegacyFavoriteIsKeptOnSyncedRow() =
        runBlocking {
            val synced = MarketEntity("1", "Bitcoin", "BTC", 60000.0, 6.0, "url", FALSE)
            marketDatabase(context, PlatformDispatcherProvider()).apply {
                marketDao().insertMarkets(listOf(synced))
                close()
            }
            createLegacyDatabase()

            val favorites = openAndGetFavorites()

            assertEquals(listOf(synced.copy(isFavorite = TRUE)), favorites)
            assertFalse(legacy.exists())
        }

    private suspend fun openAndGetFavorites(): List<MarketEntity> {
        val db = marketDatabase(context, PlatformDispatcherProvider())
        return db
            .marketDao()
            .getFavoriteMarketList()
            .first()
            .also { db.close() }
    }

    private fun createLegacyDatabase() {
        legacy.parentFile?.mkdirs()
        AndroidSQLiteDriver().open(legacy.absolutePath).apply {
            // Market.sq, as SQLDelight created it.
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS marketEntity(
                    id TEXT UNIQUE PRIMARY KEY NOT NULL,
                    name TEXT UNIQUE NOT NULL,
                    symbol TEXT UNIQUE NOT NULL,
                    currentPrice REAL NOT NULL,
                    priceChangePercentage24h REAL NOT NULL,
                    imageUrl TEXT NOT NULL,
                    isFavorite INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent(),
            )
            execSQL("INSERT INTO marketEntity VALUES ('1', 'Bitcoin', 'BTC', 50000.0, 5.0, 'url', 1)")
            execSQL("INSERT INTO marketEntity VALUES ('2', 'Ethereum', 'ETH', 3000.0, 1.0, 'url', 0)")
            close()
        }
    }
}
