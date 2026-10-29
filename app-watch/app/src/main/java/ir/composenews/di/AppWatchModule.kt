package ir.composenews.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import ir.composenews.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appWatchModule =
    module {
        viewModel { MainViewModel(get()) }
        single<HttpClientEngine> { OkHttp.create() }
    }
