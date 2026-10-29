package ir.composenews

import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// Chucker is the real library on debug and the no-op one on release (see build.gradle.kts).
private val httpEngineModule =
    module {
        single<HttpClientEngine> {
            OkHttp.create { addInterceptor(ChuckerInterceptor.Builder(androidContext()).build()) }
        }
    }

internal val backendModules =
    listOf(
        remoteDatasourceModule,
        httpEngineModule,
    )
