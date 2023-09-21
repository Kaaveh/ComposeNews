@file:Suppress("PackageNaming", "PackageName", "ImportOrdering")

package ir.composenews.core_test.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.PlatformDispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
interface DispatcherModule {
    @Binds
    fun bindDispatcherProvider(impl: PlatformDispatcherProvider): DispatcherProvider
}
