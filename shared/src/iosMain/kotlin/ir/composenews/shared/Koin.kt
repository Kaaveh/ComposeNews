package ir.composenews.shared

import ir.composenews.base.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.domain.di.domainModule
import ir.composenews.localdatasource.di.localDatasourceIosModule
import ir.composenews.localdatasource.di.localDatasourceModule
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketdetail.di.marketDetailModule
import ir.composenews.marketlist.FavoriteListViewModel
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.marketlist.di.marketListModule
import ir.composenews.remotedatasource.di.remoteDatasourceIosModule
import ir.composenews.remotedatasource.di.remoteDatasourceModule
import ir.composenews.sync.SyncScheduler
import ir.composenews.sync.di.syncModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

/**
 * Call once at launch, before any ViewModel factory. Starts the foreground sync, as the Android app does.
 *
 * Swift sees it as `doInitKoin()` (ObjC reserves the `init` prefix, even past `@ObjCName`), so `iosApp` wraps it.
 */
fun initKoin() {
    startKoin {
        modules(
            dispatcherModule,
            localDatasourceModule,
            localDatasourceIosModule,
            remoteDatasourceModule,
            remoteDatasourceIosModule,
            dataModule,
            domainModule,
            syncModule,
            marketListModule,
            marketDetailModule,
        )
    }
    KoinPlatform.getKoin().get<SyncScheduler>().schedule()
}

// Concrete types: through `UnidirectionalViewModel` Swift would only see `state` as `Any`.
fun marketListViewModel(): MarketListViewModel = KoinPlatform.getKoin().get()

fun favoriteListViewModel(): FavoriteListViewModel = KoinPlatform.getKoin().get()

fun marketDetailViewModel(): MarketDetailViewModel = KoinPlatform.getKoin().get()
