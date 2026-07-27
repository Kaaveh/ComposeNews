package ir.composenews

import ir.composenews.remotedatasource.di.remoteDatasourceModule

internal val backendModules =
    listOf(
        remoteDatasourceModule,
    )
