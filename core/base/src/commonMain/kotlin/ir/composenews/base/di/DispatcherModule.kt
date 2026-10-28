package ir.composenews.base.di

import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import org.koin.dsl.module

val dispatcherModule = module { single<DispatcherProvider> { PlatformDispatcherProvider() } }
