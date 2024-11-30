package ir.composenews.marketlist

import ir.composenews.domain.domainModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val marketListFeatureModule = module {
    viewModel {
        MarketListViewModel(get(), get(), get(), get())
    }
    includes(domainModule)
}
