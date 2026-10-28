@file:Suppress("MagicNumber")

package ir.composenews.localdatasource.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteException
import androidx.sqlite.SQLiteStatement
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import ir.composenews.base.dispatcher.DispatcherProvider

private const val DATABASE_NAME = "composenews.db"

/** The SQLDelight database used before #360. Room never opens it, it only copies from it. */
internal const val LEGACY_DATABASE_NAME = "MarketDatabase"

private const val COLUMNS = "id, name, symbol, currentPrice, priceChangePercentage24h, imageUrl, isFavorite"

fun marketDatabase(
    context: Context,
    dispatchers: DispatcherProvider,
): MarketDatabase =
    buildMarketDatabase(
        Room
            .databaseBuilder<MarketDatabase>(context, context.getDatabasePath(DATABASE_NAME).absolutePath)
            .addCallback(LegacyImportCallback(context)),
        dispatchers,
        AndroidSQLiteDriver(),
    )

/**
 * Copies the markets, favorites included, out of the legacy database and then deletes it.
 *
 * It reads the legacy file over its own connection rather than `ATTACH`: Android's framework driver
 * pools connections, so an `ATTACH` isn't seen by the next statement. It runs in `onOpen`, so a
 * failed copy doesn't stop Room from creating its database; the legacy file stays and the next
 * open tries again.
 */
private class LegacyImportCallback(
    private val context: Context,
) : RoomDatabase.Callback() {
    override fun onOpen(connection: SQLiteConnection) {
        val legacy = context.getDatabasePath(LEGACY_DATABASE_NAME)
        if (!legacy.exists()) return
        try {
            AndroidSQLiteDriver().open(legacy.absolutePath).use { copyMarkets(from = it, to = connection) }
        } catch (e: SQLiteException) {
            Log.w("MarketDatabase", "Couldn't import the legacy database", e)
            return
        }
        context.deleteDatabase(LEGACY_DATABASE_NAME)
    }
}

/**
 * Copies every row in one transaction, so a killed process leaves nothing half copied. A sync may
 * have written a row since a failed import, so a legacy favorite is set on the existing row too.
 */
private fun copyMarkets(
    from: SQLiteConnection,
    to: SQLiteConnection,
) {
    to.execSQL("BEGIN IMMEDIATE TRANSACTION")
    try {
        from.prepare("SELECT $COLUMNS FROM marketEntity").use { copyRows(it, to) }
        to.execSQL("COMMIT TRANSACTION")
    } catch (e: SQLiteException) {
        to.execSQL("ROLLBACK TRANSACTION")
        throw e
    }
}

private fun copyRows(
    select: SQLiteStatement,
    to: SQLiteConnection,
) {
    to.prepare("INSERT OR IGNORE INTO marketEntity ($COLUMNS) VALUES (?, ?, ?, ?, ?, ?, ?)").use { insert ->
        to.prepare("UPDATE marketEntity SET isFavorite = MAX(isFavorite, ?) WHERE id = ?").use { favorite ->
            while (select.step()) {
                insert.bindText(1, select.getText(0))
                insert.bindText(2, select.getText(1))
                insert.bindText(3, select.getText(2))
                insert.bindDouble(4, select.getDouble(3))
                insert.bindDouble(5, select.getDouble(4))
                insert.bindText(6, select.getText(5))
                insert.bindLong(7, select.getLong(6))
                insert.step()
                insert.reset()
                favorite.bindLong(1, select.getLong(6))
                favorite.bindText(2, select.getText(0))
                favorite.step()
                favorite.reset()
            }
        }
    }
}
