@file:Suppress("PackageNaming", "PackageName", "ImportOrdering")

package ir.composenews.core_test.di

import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.PlatformDispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DispatcherModule = module {
    singleOf(::PlatformDispatcherProvider).bind<DispatcherProvider>()
}