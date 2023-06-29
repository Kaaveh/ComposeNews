package com.github.mohammadsianaki.core_test.di

import com.github.mohammadsianaki.core_test.DispatcherProvider
import com.github.mohammadsianaki.core_test.PlatformDispatcherProvider
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