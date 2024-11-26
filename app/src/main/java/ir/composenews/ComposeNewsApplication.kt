package ir.composenews

import android.app.Application
import ir.composenews.data.di.repositoryModule
import ir.composenews.marketdetail.marketDetailFeatureModule
import ir.composenews.marketlist.marketListFeatureModule
import ir.composenews.sync.Sync
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ComposeNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Sync.init(this)

        startKoin {
            androidContext(this@ComposeNewsApplication)
            androidLogger(Level.DEBUG)

            modules(
                listOf(
                    marketListFeatureModule, marketDetailFeatureModule,
                    repositoryModule,
                )
            )
        }
    }
}