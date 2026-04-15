package ir.composenews.data.di

import ir.composenews.data.repository.MarketRepositoryImpl
import ir.composenews.domain.repository.MarketRepository
import org.koin.dsl.module

val dataModule =
    module {
        single<MarketRepository> { MarketRepositoryImpl(get(), get()) }
    }
