@file:Suppress("ktlint")

package ir.composenews.data.di

import ir.composenews.data.repository.MarketRepositoryImpl
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.remotedatasource.di.apiModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(
        apiModule,
        localDatasourceModule,
    )
    singleOf(::MarketRepositoryImpl) bind MarketRepository::class
}
