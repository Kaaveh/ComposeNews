package ir.composenews.sync.di

import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.sync.ForegroundSyncScheduler
import ir.composenews.sync.SyncScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val syncModule =
    module {
        single<SyncScheduler> {
            ForegroundSyncScheduler(get(), CoroutineScope(SupervisorJob() + get<DispatcherProvider>().io))
        }
    }
