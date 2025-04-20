@file:Suppress(
    "TopLevelPropertyNaming",
    "Indentation",
    "SwallowedException",
    "TooGenericExceptionCaught",
    "ktlint:standard:chain-method-continuation",
    "ktlint:standard:no-empty-first-line-in-class-body",
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:blank-line-before-declaration",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.sync.worker

import ir.composenews.domain.use_case.SyncMarketListUseCase
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val workerModule = module {

    factoryOf(::SyncMarketListUseCase)
    workerOf(::SyncWorker)
}
