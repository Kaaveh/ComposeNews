package ir.composenews

import ir.composenews.remotedatasource.api.MarketsApi
import org.koin.dsl.module

internal val fixtureRemoteDataSourceModule = module {
    single<MarketsApi> { FixtureMarketsApi() }
}

/**
 * This replaces only MarketsApi. Your existing MarketRepositoryImpl, MarketsPagingSource,
 * SQLDelight storage, domain use cases, and UI remain active.
 */
internal val backendModules = listOf(
    fixtureRemoteDataSourceModule
)