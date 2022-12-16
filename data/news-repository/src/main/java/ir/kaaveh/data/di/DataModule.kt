package ir.kaaveh.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ir.kaaveh.data.repository.NewsRepositoryImpl
import ir.kaaveh.domain.repository.NewsRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
interface DataModule {

    @Binds
    fun bindNewsRepository(
        newsRepository: NewsRepositoryImpl,
    ): NewsRepository

}