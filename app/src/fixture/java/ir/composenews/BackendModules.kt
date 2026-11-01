package ir.composenews

import io.ktor.client.engine.HttpClientEngine
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import org.koin.dsl.module

private val fixtureHttpEngineModule =
    module {
        single<HttpClientEngine> { FixtureMarketsEngine.create() }
    }

/**
 * This replaces only the HTTP engine. MarketsApiImpl, the Ktor client, MarketRepositoryImpl,
 * Room storage, domain use cases, and UI remain active.
 */
internal val backendModules =
    listOf(
        remoteDatasourceModule,
        fixtureHttpEngineModule,
    )
