package ir.composenews.tv.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val appTvModule =
    module {
        single<HttpClientEngine> { OkHttp.create() }
    }
