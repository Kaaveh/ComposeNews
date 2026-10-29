package ir.composenews.tv.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import ir.composenews.tv.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appTvModule =
    module {
        viewModel { MainViewModel(get()) }
        single<HttpClientEngine> { OkHttp.create() }
    }
