package ir.composenews.data.di

import ir.composenews.core_test.di.DispatcherModule
import ir.composenews.data.repository.MarketRepositoryImpl
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.remotedatasource.di.apiModule
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::MarketRepositoryImpl).bind<MarketRepository>()

    includes(
        DispatcherModule,
        apiModule,
        remoteDatasourceModule,
        localDatasourceModule,
    )
}
