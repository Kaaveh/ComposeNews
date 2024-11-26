package ir.composenews

import android.app.Application
import ir.composenews.data.di.repositoryModule
import ir.composenews.marketdetail.marketDetailFeatureModule
import ir.composenews.marketlist.marketListFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class ComposeNewsWearApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ComposeNewsWearApplication)
            androidLogger(Level.DEBUG)

            modules(
                listOf(
                    mainViewModelModule,
                    marketListFeatureModule, marketDetailFeatureModule,

                    repositoryModule,
                )
            )
        }
    }
}