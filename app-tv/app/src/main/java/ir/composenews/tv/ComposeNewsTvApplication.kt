package ir.composenews.tv

import android.app.Application
import ir.composenews.base.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.domain.di.domainModule
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.marketdetail.di.marketDetailModule
import ir.composenews.marketlist.di.marketListModule
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import ir.composenews.tv.di.appTvModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComposeNewsTvApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComposeNewsTvApplication)
            modules(
                dispatcherModule,
                localDatasourceModule,
                remoteDatasourceModule,
                dataModule,
                domainModule,
                marketListModule,
                marketDetailModule,
                appTvModule,
            )
        }
    }
}
