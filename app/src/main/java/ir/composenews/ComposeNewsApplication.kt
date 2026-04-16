package ir.composenews

import android.app.Application
import androidx.work.Configuration
import ir.composenews.core_test.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.domain.di.domainModule
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.marketdetail.di.marketDetailModule
import ir.composenews.marketlist.di.marketListModule
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import ir.composenews.sync.Sync
import ir.composenews.sync.di.syncModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class ComposeNewsApplication :
    Application(),
    Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(getKoin().get())
                .build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComposeNewsApplication)
            workManagerFactory()
            modules(
                dispatcherModule,
                localDatasourceModule,
                remoteDatasourceModule,
                dataModule,
                domainModule,
                syncModule,
                marketListModule,
                marketDetailModule,
            )
        }
        Sync.init(this)
    }
}
