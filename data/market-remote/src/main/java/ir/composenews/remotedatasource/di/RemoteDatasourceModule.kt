package ir.composenews.remotedatasource.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import ir.composenews.remotedatasource.util.HttpRoutes
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object RemoteDatasourceModule {

    @Provides
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): HttpClient {
        return HttpClient(OkHttp) {
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
                addInterceptor(ChuckerInterceptor.Builder(context).build())
            }
        }
    }
}
