package ir.kaaveh.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kaaveh.data.repository.MarketRepositoryImpl
import ir.kaaveh.domain.repository.MarketRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindMarketRepository(
        marketsRepository: MarketRepositoryImpl,
    ): MarketRepository

}