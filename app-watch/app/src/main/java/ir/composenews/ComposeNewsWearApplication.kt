package ir.composenews

import android.app.Application
import ir.composenews.core_test.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.di.appWatchModule
import ir.composenews.domain.di.domainModule
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.marketdetail.di.marketDetailModule
import ir.composenews.marketlist.di.marketListModule
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComposeNewsWearApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComposeNewsWearApplication)
            modules(
                dispatcherModule,
                localDatasourceModule,
                remoteDatasourceModule,
                dataModule,
                domainModule,
                marketListModule,
                marketDetailModule,
                appWatchModule,
            )
        }
    }
}
