package ir.composenews.remotedatasource.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.composenews.remotedatasource.api.BASE_URL
import ir.composenews.remotedatasource.api.MarketsApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDatasourceModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        app: Application,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor(app))
        .addNetworkInterceptor(
            HttpLoggingInterceptor { message ->
                println("LOG-NET: $message")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .build()

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun provideMarketsApi(
        retrofit: Retrofit,
    ): MarketsApi = retrofit.create(MarketsApi::class.java)
}
