package ir.composenews.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.navigation.MainContract
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.utils.ContentType
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
            is MainContract.Event.SetMarket -> setMarket(event.market, event.contentType)
        }
    }

    private fun setMarket(market: MarketModel?, contentType: ContentType) = viewModelScope.launch {
        mutableState.emit(
            mutableState.value.copy(
                market,
                isDetailOnlyOpen = contentType == ContentType.SINGLE_PANE
            )
        )
    }

    fun closeDetailScreen() = viewModelScope.launch {
        mutableState.emit(
            mutableState.value.copy(
                isDetailOnlyOpen = false
            )
        )
    }
}