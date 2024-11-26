package ir.composenews.remotedatasource.di

import ir.composenews.remotedatasource.okttpClient.provideHttpClient
import org.koin.dsl.module

val remoteDatasourceModule = module {
    single { provideHttpClient(get()) }
}

