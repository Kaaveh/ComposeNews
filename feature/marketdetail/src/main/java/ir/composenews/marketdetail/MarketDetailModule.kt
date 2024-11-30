package ir.composenews.marketdetail

import ir.composenews.domain.domainModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val marketDetailFeatureModule = module {
    viewModel {
        MarketDetailViewModel(get(), get(), get(), get())
    }

    includes(domainModule)
}
