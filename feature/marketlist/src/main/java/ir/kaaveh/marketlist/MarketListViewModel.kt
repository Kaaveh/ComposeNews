package ir.kaaveh.marketlist

import androidx.lifecycle.viewModelScope
import com.github.mohammadsianaki.core_test.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseContract
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.use_case.GetFavoriteMarketListUseCase
import ir.kaaveh.domain.use_case.GetMarketListUseCase
import ir.kaaveh.domain.use_case.ToggleFavoriteMarketListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketListViewModel @Inject constructor(
    private val getMarketListUseCase: GetMarketListUseCase,
    private val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MarketListContract {

    private val mutableState = MutableStateFlow(MarketListContract.State())
    override val state: StateFlow<MarketListContract.State> = mutableState.asStateFlow()

    override fun event(event: MarketListContract.Event) = when (event) {
        MarketListContract.Event.OnGetMarketList -> getData()
        MarketListContract.Event.OnRefresh -> getData(isRefreshing = true)
        is MarketListContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.market)
        is MarketListContract.Event.OnSetShowFavoriteList -> onSetShowFavoriteList(showFavoriteList = event.showFavoriteList)
    }

    private fun onSetShowFavoriteList(showFavoriteList: Boolean) {
        mutableState.update {
            it.copy(showFavoriteList = showFavoriteList)
        }
    }

    private fun getData(isRefreshing: Boolean = false) {
        if (isRefreshing)
            mutableState.update {
                MarketListContract.State(
                    refreshing = true,
                )
            }
        viewModelScope.launch {
            if (mutableState.value.showFavoriteList)
                getFavoriteMarketList()
            else
                getMarketList()
        }
    }

    private suspend fun getMarketList() = getMarketListUseCase()
        .onEach { result ->
            mutableState.update {
                MarketListContract.State(marketList = result)
            }
        }
        .catch { exception ->
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    errorMessage = exception.localizedMessage ?: "An unexpected error occurred."
                )
            }
        }
        .launchIn(viewModelScope)

    private fun getFavoriteMarketList() =
        getFavoriteMarketListUseCase().onEach { newList ->
            mutableState.update {
                it.copy(marketList = newList)
            }
        }.launchIn(viewModelScope)

    private fun onFavoriteClick(news: Market) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(news)
            }
        }
    }

}