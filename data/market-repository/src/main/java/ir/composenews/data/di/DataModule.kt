package ir.composenews.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.composenews.data.repository.MarketRepositoryImpl
import ir.composenews.domain.repository.MarketRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindMarketRepository(
        marketsRepository: MarketRepositoryImpl,
    ): MarketRepository
}
