package ir.composenews.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDaoImpl
import ir.composenews.remotedatasource.test.FakeMarketsApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

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
            marketList.shouldBeEmpty()
        }
    }

    "Get market list from internet then read it from db" {
        runTest {
            repositoryImpl.syncMarketList()

            val marketList = repositoryImpl.getMarketList().first()
            marketList.shouldNotBeEmpty()
        }
    }

    "Toggle favorite market state" {
        runTest {
            repositoryImpl.syncMarketList()

            val oldMarket = repositoryImpl.getMarketList().first().first()
            oldMarket.isFavorite shouldBeEqual false

            repositoryImpl.toggleFavoriteMarket(oldMarket)
            val market = repositoryImpl.getMarketList().first().first()
            market.isFavorite shouldBeEqual true
        }
    }
})
