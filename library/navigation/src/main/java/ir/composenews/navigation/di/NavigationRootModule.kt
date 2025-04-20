package ir.composenews.navigation.di

import ir.composenews.marketdetail.di.marketDetailModule
import ir.composenews.marketlist.di.marketListModule
import org.koin.dsl.module

val navigationRootModule =
    module {
        includes(
            marketListModule,
            marketDetailModule,
        )
    }
