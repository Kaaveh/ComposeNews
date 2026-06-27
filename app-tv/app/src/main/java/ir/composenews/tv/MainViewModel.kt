package ir.composenews.tv

import androidx.lifecycle.viewModelScope
import ir.composenews.apptv.navigation.MainContract
import ir.composenews.base.BaseViewModel
import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider),
    MainContract {
    private val mutableState = MutableStateFlow(MainContract.State())

    override val state: StateFlow<MainContract.State> = mutableState.asStateFlow()

    override fun event(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.SetMarket -> setMarket(event.market)
        }
    }

    private fun setMarket(market: MarketModel?) =
        viewModelScope.launch {
            mutableState.emit(
                mutableState.value.copy(
                    market = market,
                ),
            )
        }
}
