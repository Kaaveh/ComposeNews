package ir.composenews.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseViewModel
import ir.composenews.base.MainContract
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.model.Market
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
            is MainContract.Event.SetMarket -> setMarket(event.market as Market?, event.contentType)
        }
    }

    private fun setMarket(market: Market?, contentType: ContentType) = viewModelScope.launch {
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