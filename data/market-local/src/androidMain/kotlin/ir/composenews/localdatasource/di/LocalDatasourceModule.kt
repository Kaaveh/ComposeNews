package ir.composenews.localdatasource.di

import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDatabase
import ir.composenews.localdatasource.database.marketDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDatasourceModule =
    module {
        single<MarketDatabase> { marketDatabase(androidContext(), get()) }
        factory<MarketDao> { get<MarketDatabase>().marketDao() }
    }
