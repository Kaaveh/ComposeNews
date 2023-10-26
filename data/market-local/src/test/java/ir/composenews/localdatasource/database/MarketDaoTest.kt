package ir.composenews.localdatasource.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.test.favoriteLocalMarketDto
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MarketDaoTest {

    private lateinit var marketDao: MarketDao

    @Before
    fun createDb() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MarketDatabase.Schema.create(driver)
        val db = MarketDatabase(driver)
        marketDao = MarketDaoImpl(db)
    }

    @Test
    @Throws(Exception::class)
    fun emptyTableAtDbInitialization() = runTest {
        val marketList = marketDao.getMarketList().first()
        assertTrue(marketList.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertNewsToDb() = runTest {
        marketDao.insertMarket(favoriteLocalMarketDto)
        val newsList = marketDao.getMarketList().first()
        assertTrue(newsList.contains(favoriteLocalMarketDto))
    }
}
