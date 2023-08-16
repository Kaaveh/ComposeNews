package ir.composenews.core_test.di

import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.PlatformDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DispatcherModule {

    @Binds
    fun bindDispatcherProvider(impl: PlatformDispatcherProvider): DispatcherProvider
}