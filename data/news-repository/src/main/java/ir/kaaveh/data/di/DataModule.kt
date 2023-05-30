package ir.kaaveh.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kaaveh.data.repository.MarketsRepositoryImpl
import ir.kaaveh.domain.repository.MarketsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindMarketRepository(
        marketsRepository: MarketsRepositoryImpl,
    ): MarketsRepository

}