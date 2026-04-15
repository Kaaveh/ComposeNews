@file:Suppress("ktlint")

package ir.composenews.remotedatasource.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.api.MarketsApiImpl
import ir.composenews.remotedatasource.util.HttpRoutes
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteDatasourceModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            defaultRequest {
                url(urlString = HttpRoutes.BASE_URL)
            }

            install(Logging) {
                level = LogLevel.BODY

                logger = object : Logger {
                    override fun log(message: String) {
                        println("LOG-NET: $message")
                    }
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            engine {
                addInterceptor(ChuckerInterceptor.Builder(androidContext()).build())
            }
        }
    }
    single<MarketsApi> { MarketsApiImpl(get()) }
}
