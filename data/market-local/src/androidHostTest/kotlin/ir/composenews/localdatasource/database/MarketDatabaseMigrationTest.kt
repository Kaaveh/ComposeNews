package ir.composenews.localdatasource.database

import android.content.Context
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.core.app.ApplicationProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketDatabaseMigrationTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun givenVersion1Database_whenRoomOpens_thenFavoritesAreKeptWithoutTheNewColumns() =
        runBlocking {
            createVersion1Database()

            val db = marketDatabase(context, PlatformDispatcherProvider())
            val favorites = db.marketDao().getFavoriteMarketList().first()
            db.close()

            assertEquals(listOf(MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE)), favorites)
        }

    /** The schema in `schemas/.../1.json`, as Room created it. */
    private fun createVersion1Database() {
        val file = context.getDatabasePath("composenews.db")
        file.parentFile?.mkdirs()
        AndroidSQLiteDriver().open(file.absolutePath).apply {
            execSQL(
                "CREATE TABLE IF NOT EXISTS `marketEntity` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, " +
                    "`symbol` TEXT NOT NULL, `currentPrice` REAL NOT NULL, `priceChangePercentage24h` REAL NOT NULL, " +
                    "`imageUrl` TEXT NOT NULL, `isFavorite` INTEGER NOT NULL, PRIMARY KEY(`id`))",
            )
            execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
            execSQL(
                "INSERT OR REPLACE INTO room_master_table (id,identity_hash) " +
                    "VALUES(42, 'e97112757f8073f1c5644dd0e2377c88')",
            )
            execSQL("INSERT INTO marketEntity VALUES ('1', 'Bitcoin', 'BTC', 50000.0, 5.0, 'url', 1)")
            execSQL("INSERT INTO marketEntity VALUES ('2', 'Ethereum', 'ETH', 3000.0, 1.0, 'url', 0)")
            execSQL("PRAGMA user_version = 1")
            close()
        }
    }
}
