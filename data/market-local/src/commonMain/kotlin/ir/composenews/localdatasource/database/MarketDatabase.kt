package ir.composenews.localdatasource.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.SQLiteDriver
import ir.composenews.base.dispatcher.DispatcherProvider

@Database(entities = [MarketEntity::class], version = 1)
@ConstructedBy(MarketDatabaseConstructor::class)
abstract class MarketDatabase : RoomDatabase() {
    abstract fun marketDao(): MarketDao
}

// The Room compiler generates the actuals.
@Suppress("KotlinNoActualForExpect")
expect object MarketDatabaseConstructor : RoomDatabaseConstructor<MarketDatabase> {
    override fun initialize(): MarketDatabase
}

/** Android passes `AndroidSQLiteDriver` (the system SQLite); iOS will pass `BundledSQLiteDriver` (#368). */
fun buildMarketDatabase(
    builder: RoomDatabase.Builder<MarketDatabase>,
    dispatchers: DispatcherProvider,
    driver: SQLiteDriver,
): MarketDatabase =
    builder
        .setDriver(driver)
        .setQueryCoroutineContext(dispatchers.io)
        .build()
