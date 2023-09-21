@file:Suppress("MaxLineLength", "TooGenericExceptionCaught")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseContract
import ir.composenews.base.BaseViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import kotlinx.collections.immutable.toPersistentList
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
    private val syncMarketListUseCase: SyncMarketListUseCase,
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
        is MarketListContract.Event.OnSetShowFavoriteList -> onSetShowFavoriteList(
            showFavoriteList = event.showFavoriteList,
        )
    }

    private fun onSetShowFavoriteList(showFavoriteList: Boolean) {
        mutableState.update {
            it.copy(showFavoriteList = showFavoriteList)
        }
    }

    private fun getData(isRefreshing: Boolean = false) {
        if (isRefreshing) {
            mutableState.update {
                it.copy(refreshing = true)
            }
        }
        viewModelScope.launch {
            if (mutableState.value.showFavoriteList) {
                getFavoriteMarketList()
            } else {
                getMarketList()
            }
        }
    }

    private suspend fun getMarketList() {
        mutableBaseState.update { BaseContract.BaseState.OnLoading }
        try {
            syncMarketListUseCase()
            mutableBaseState.update { BaseContract.BaseState.OnSuccess }
        } catch (e: Exception) {
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    errorMessage = e.localizedMessage ?: "An unexpected error occurred.",
                )
            }
        }
        getMarketListUseCase()
            .onEach { result ->
                mutableState.update {
                    it.copy(marketList = result.toPersistentList(), refreshing = false)
                }
                mutableBaseState.update { BaseContract.BaseState.OnSuccess }
            }
            .catch { exception ->
                mutableBaseState.update {
                    BaseContract.BaseState.OnError(
                        errorMessage = exception.localizedMessage ?: "An unexpected error occurred.",
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getFavoriteMarketList() {
        getFavoriteMarketListUseCase().onEach { newList ->
            mutableState.update {
                it.copy(marketList = newList.toPersistentList(), refreshing = false)
            }
        }.launchIn(viewModelScope)
    }

    private fun onFavoriteClick(news: Market) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(news)
            }
        }
    }
}
