package ir.kaaveh.core_test.di

import ir.kaaveh.core_test.dispatcher.DispatcherProvider
import ir.kaaveh.core_test.dispatcher.PlatformDispatcherProvider
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