package ir.composenews.localdatasource.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.equals.shouldBeEqual
import ir.composenews.core_test.SuspendSpec
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.test.favoriteMarketEntity
import kotlinx.coroutines.flow.first

class MarketDaoTest : SuspendSpec({
    lateinit var marketDao: MarketDao

    beforeSpec {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MarketDatabase.Schema.create(driver)
        val db = MarketDatabase(driver)
        marketDao = MarketDaoImpl(db)
    }

    "emptyTableAtDbInitialization" {
        val marketList = marketDao.getMarketList().first()
        marketList.size shouldBeEqual 0
    }

    "insertMarketToDb" {
        marketDao.insertMarket(favoriteMarketEntity)
        val marketList = marketDao.getMarketList().first()
        marketList shouldContain favoriteMarketEntity
    }
})
