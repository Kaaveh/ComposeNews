@file:OptIn(KoinExperimentalAPI::class)

package ir.composenews

import android.app.Application
import ir.composenews.navigation.di.navigationRootModule
import ir.composenews.sync.Sync
import ir.composenews.sync.worker.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

class ComposeNewsApplication : Application(), KoinStartup {

    override fun onCreate() {
        super.onCreate()
//        Sync.init(this) // TODO causes crash. temporary commented so our workmanager initializer disabled
    }

        override fun onKoinStartup() = KoinConfiguration {
        androidContext(this@ComposeNewsApplication)
        workManagerFactory()
        modules(
            workerModule,
            navigationRootModule,
        )
    }

}
