@file:Suppress("MaxLineLength", "TooGenericExceptionCaught")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseContract
import ir.composenews.base.BaseViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
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
        getMarketListUseCase()
            .onEach { result ->
                mutableState.update { prevState ->
                    prevState.copy(
                        marketList = result.map { it.toMarketModel() }.toPersistentList(),
                        refreshing = false,
                        showFavoriteEmptyState = result.isEmpty(),
                    )
                }
                mutableBaseState.update { BaseContract.BaseState.OnSuccess }
            }
            .catch { exception ->
                mutableBaseState.update {
                    BaseContract.BaseState.OnError(
                        errors = Errors.ExceptionError(message = exception.message),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getFavoriteMarketList() {
        getFavoriteMarketListUseCase().onEach { newList ->
            mutableState.update { prevState ->
                prevState.copy(
                    marketList = newList.map { it.toMarketModel() }.toPersistentList(),
                    refreshing = false,
                    showFavoriteEmptyState = newList.isEmpty(),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun onFavoriteClick(news: MarketModel) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(news.toMarket())
            }
        }
    }
}
