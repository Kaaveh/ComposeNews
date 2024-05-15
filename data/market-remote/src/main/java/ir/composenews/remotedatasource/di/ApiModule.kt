package ir.composenews.remotedatasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.api.MarketsApiImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    abstract fun bindMarketsApi(marketsApi: MarketsApiImpl): MarketsApi
}
