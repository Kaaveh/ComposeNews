package ir.composenews.localdatasource.di

import ir.composenews.localdatasource.database.MarketDatabase
import ir.composenews.localdatasource.database.marketDatabase
import org.koin.dsl.module

val localDatasourceIosModule =
    module {
        single<MarketDatabase> { marketDatabase(get()) }
    }
