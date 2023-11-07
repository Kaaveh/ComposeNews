package ir.composenews.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.kotest.core.spec.style.StringSpec
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDaoImpl
import ir.composenews.remotedatasource.test.FakeMarketsApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class NewsRepositoryImplTest : StringSpec({

    lateinit var marketDao: MarketDao
    val api = FakeMarketsApi()
    lateinit var repositoryImpl: MarketRepositoryImpl

    beforeTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MarketDatabase.Schema.create(driver)
        val db = MarketDatabase(driver)
        marketDao = MarketDaoImpl(db)
        repositoryImpl = MarketRepositoryImpl(api = api, dao = marketDao)
    }

    "Initial db empty state" {
        runTest {
            val marketList = repositoryImpl.getMarketList().first()
            marketList.isEmpty()
        }
    }

    "Get market list from internet then read it from db" {
        runTest {
            repositoryImpl.syncMarketList()

            val marketList = repositoryImpl.getMarketList().first()
            marketList.isNotEmpty()
        }
    }

    "Toggle favorite market state" {
        runTest {
            repositoryImpl.syncMarketList()

            val oldMarket = repositoryImpl.getMarketList().first().first()
            assertFalse(oldMarket.isFavorite)

            repositoryImpl.toggleFavoriteMarket(oldMarket)
            val market = repositoryImpl.getMarketList().first().first()
            assertTrue(market.isFavorite)
        }
    }
})
