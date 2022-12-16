package ir.kaaveh.remotedatasource.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kaaveh.remotedatasource.api.BASE_URL
import ir.kaaveh.remotedatasource.api.NewsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDatasourceModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(app: Application): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor(app))
        .addNetworkInterceptor(HttpLoggingInterceptor { message ->
                println("LOG-NET: $message")
            }.apply {
                level= HttpLoggingInterceptor.Level.BODY
            })
        .build()

    @Singleton
    @Provides
    fun provideNewsApi(
        client: OkHttpClient,
    ): NewsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

}