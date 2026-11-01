package ir.composenews.remotedatasource.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/** The engine [remoteDatasourceModule] needs. On Android each app provides its own. */
val remoteDatasourceIosModule =
    module {
        single<HttpClientEngine> { Darwin.create() }
    }
