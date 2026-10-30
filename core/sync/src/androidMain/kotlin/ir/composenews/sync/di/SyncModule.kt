package ir.composenews.sync.di

import ir.composenews.sync.SyncScheduler
import ir.composenews.sync.WorkManagerSyncScheduler
import ir.composenews.sync.worker.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val syncModule =
    module {
        workerOf(::SyncWorker)
        single<SyncScheduler> { WorkManagerSyncScheduler(androidContext()) }
    }
