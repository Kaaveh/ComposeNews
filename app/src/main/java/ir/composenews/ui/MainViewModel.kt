package ir.kaaveh.composenews.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.base.BaseViewModel
import ir.kaaveh.base.MainContract
import ir.kaaveh.core_test.dispatcher.DispatcherProvider
import ir.kaaveh.designsystem.utils.ContentType
import ir.kaaveh.domain.model.Market
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
): BaseViewModel(dispatcherProvider), MainContract {

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
                isDetailOnlyOpen = contentType == ContentType.SINGLE_PANE)
        )
    }

    fun closeDetailScreen() = viewModelScope.launch {
        mutableState.emit(
            mutableState.value.copy(isDetailOnlyOpen = false)
        )
    }

}