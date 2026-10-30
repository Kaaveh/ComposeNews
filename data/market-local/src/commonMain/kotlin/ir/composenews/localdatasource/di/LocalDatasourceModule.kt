package ir.composenews.localdatasource.di

import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDatabase
import org.koin.dsl.module

/** Needs a `MarketDatabase` from the platform module (`localDatasourceAndroidModule` on Android). */
val localDatasourceModule =
    module {
        factory<MarketDao> { get<MarketDatabase>().marketDao() }
    }
