package ir.composenews

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.appwatch.navigation.MainContract
import ir.composenews.base.BaseViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MainContract {

    private val mutableState = MutableStateFlow(MainContract.State())
    override val state: StateFlow<MainContract.State> = mutableState.asStateFlow()
    override fun event(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.SetMarket -> setMarket(event.market)
        }
    }

    private fun setMarket(market: MarketModel?) = viewModelScope.launch {
        mutableState.emit(
            mutableState.value.copy(
                market
            )
        )
    }
}