package ir.composenews.localdatasource.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDaoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDatasourceModule =
    module {
        single<MarketDatabase> {
            MarketDatabase(AndroidSqliteDriver(MarketDatabase.Schema, androidContext(), "MarketDatabase"))
        }
        factory<MarketDao> { MarketDaoImpl(get()) }
    }
