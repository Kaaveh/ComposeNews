package ir.composenews.localdatasource.database

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

/**
 * An empty in-memory database: on Android it needs Robolectric's context, so only `MarketDaoTest` calls it there.
 */
expect fun inMemoryMarketDatabase(): MarketDatabase

// The DAO checks. Android runs them from the Robolectric `MarketDaoTest`, iOS from the Kotest `MarketDaoSpec`.

suspend fun MarketDao.checkInsertedMarketIsListed() {
    val market = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE, marketCapRank = 1)

    insertMarkets(listOf(market))

    getMarketList().first() shouldBe listOf(market)
}

suspend fun MarketDao.checkFavoriteIsListed() {
    val favorite = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", TRUE)
    insertMarkets(listOf(favorite, MarketEntity("2", "Ethereum", "ETH", 3000.0, 1.0, "url", FALSE)))

    getFavoriteMarketList().first() shouldBe listOf(favorite)
}

suspend fun MarketDao.checkReinsertUpdatesDataAndKeepsFavorite() {
    val market = MarketEntity("1", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE, marketCapRank = 1)
    insertMarkets(listOf(market))
    updateFavoriteStatus(id = "1", isFavorite = TRUE)

    insertMarkets(listOf(market.copy(currentPrice = 50001.0, priceChangePercentage24h = 5.000001)))

    getMarketList().first() shouldBe
        listOf(market.copy(currentPrice = 50001.0, priceChangePercentage24h = 5.000001, isFavorite = TRUE))
    getMarketById("1").first()?.isFavorite shouldBe TRUE
}

suspend fun MarketDao.checkMarketsSharingNameAndSymbolAreBothStored() {
    val markets =
        listOf(
            MarketEntity("a", "Pepe", "PEPE", 1.0, 1.0, "url", FALSE, marketCapRank = 1),
            MarketEntity("b", "Pepe", "PEPE", 2.0, 2.0, "url", FALSE, marketCapRank = 2),
        )

    insertMarkets(markets)

    getMarketList().first().sortedBy { it.id } shouldBe markets
}

suspend fun MarketDao.checkOnlyRankedMarketsAreListedByRank() {
    val second = MarketEntity("eth", "Ethereum", "ETH", 3000.0, 1.0, "url", FALSE, marketCapRank = 2)
    val first = MarketEntity("btc", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE, marketCapRank = 1)
    val unranked = MarketEntity("old", "Old", "OLD", 1.0, 1.0, "url", TRUE)

    insertMarkets(listOf(second, unranked, first))

    getMarketList().first() shouldBe listOf(first, second)
}

suspend fun MarketDao.checkDroppedFavoriteIsUnlistedButStaysFavorite() {
    val bitcoin = MarketEntity("btc", "Bitcoin", "BTC", 50000.0, 5.0, "url", FALSE, 1, 3.5e10, "1.0,2.0")
    val dropped = MarketEntity("old", "Old", "OLD", 1.0, 1.0, "url", FALSE, marketCapRank = 2)
    replaceTopMarkets(listOf(bitcoin, dropped))
    updateFavoriteStatus(id = "old", isFavorite = TRUE)

    replaceTopMarkets(listOf(bitcoin.copy(sparkline7d = "2.0,3.0")))

    getMarketList().first() shouldBe listOf(bitcoin.copy(sparkline7d = "2.0,3.0"))
    getFavoriteMarketList().first() shouldBe listOf(dropped.copy(isFavorite = TRUE, marketCapRank = null))
}
