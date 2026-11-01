package ir.composenews.localdatasource.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.kotest.core.spec.style.StringSpec
import ir.composenews.base.dispatcher.PlatformDispatcherProvider

actual fun inMemoryMarketDatabase(): MarketDatabase =
    buildMarketDatabase(
        Room.inMemoryDatabaseBuilder<MarketDatabase>(),
        PlatformDispatcherProvider(),
        BundledSQLiteDriver(),
    )

class MarketDaoSpec :
    StringSpec({
        listOf<Pair<String, suspend MarketDao.() -> Unit>>(
            "An inserted market is listed" to { checkInsertedMarketIsListed() },
            "An inserted favorite is listed in favorites" to { checkFavoriteIsListed() },
            "Inserting a favorite again updates its data and keeps it favorite" to {
                checkReinsertUpdatesDataAndKeepsFavorite()
            },
            "Markets sharing a name and symbol are both stored" to { checkMarketsSharingNameAndSymbolAreBothStored() },
            "Only ranked markets are listed, by rank" to { checkOnlyRankedMarketsAreListedByRank() },
            "A favorite dropping out of the top markets is unlisted but stays favorite" to {
                checkDroppedFavoriteIsUnlistedButStaysFavorite()
            },
        ).forEach { (name, check) ->
            name {
                val db = inMemoryMarketDatabase()
                try {
                    db.marketDao().check()
                } finally {
                    db.close()
                }
            }
        }
    })
