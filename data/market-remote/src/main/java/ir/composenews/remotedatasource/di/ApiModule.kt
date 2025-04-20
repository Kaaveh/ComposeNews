@file:Suppress(
    "ktlint:standard:no-empty-first-line-in-class-body",
)

package ir.composenews.remotedatasource.di

import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.api.MarketsApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiModule =
    module {
        includes(
            remoteDatasourceModule,
        )
        singleOf(::MarketsApiImpl) bind MarketsApi::class
    }
