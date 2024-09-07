package ir.composenews.localdatasource.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDaoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatasourceModule {

    @Singleton
    @Provides
    fun provideMarketsDatabase(@ApplicationContext context: Context): MarketDatabase {
        val driver: SqlDriver = AndroidSqliteDriver(MarketDatabase.Schema, context, "MarketDatabase")
        return MarketDatabase(driver)
    }

    @Provides
    fun provideMarketsDao(dao: MarketDaoImpl): MarketDao = dao
}
