@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test.di

import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.PlatformDispatcherProvider
import org.koin.dsl.module

val dispatcherModule =
    module {
        single<DispatcherProvider> { PlatformDispatcherProvider() }
    }
