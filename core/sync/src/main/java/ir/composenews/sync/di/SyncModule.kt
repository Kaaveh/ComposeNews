package ir.composenews.sync.di

import ir.composenews.sync.worker.SyncWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val syncModule =
    module {
        workerOf(::SyncWorker)
    }
