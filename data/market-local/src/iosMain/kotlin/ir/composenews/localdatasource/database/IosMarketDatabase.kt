package ir.composenews.localdatasource.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.composenews.base.dispatcher.DispatcherProvider
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DATABASE_NAME = "composenews.db"

/** iOS has no SQLDelight database to import, so there's no legacy callback. */
fun marketDatabase(dispatchers: DispatcherProvider): MarketDatabase =
    buildMarketDatabase(
        Room.databaseBuilder<MarketDatabase>(name = "${documentDirectory()}/$DATABASE_NAME"),
        dispatchers,
        BundledSQLiteDriver(),
    )

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )
    return requireNotNull(url?.path) { "No Documents directory" }
}
