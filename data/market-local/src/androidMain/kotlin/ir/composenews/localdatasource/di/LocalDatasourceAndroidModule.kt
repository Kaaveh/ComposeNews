package ir.composenews.localdatasource.di

import ir.composenews.localdatasource.database.MarketDatabase
import ir.composenews.localdatasource.database.marketDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDatasourceAndroidModule =
    module {
        single<MarketDatabase> { marketDatabase(androidContext(), get()) }
    }
