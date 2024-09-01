@file:Suppress("ImportOrdering")

package ir.composenews.marketdetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseContract
import ir.composenews.base.BaseViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.model.MarketModel
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
class MarketDetailViewModel @Inject constructor(
    private val getMarketChartUseCase: GetMarketChartUseCase,
    private val getMarketDetailUseCase: GetMarketDetailUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MarketDetailContract {

    private val mutableState = MutableStateFlow(MarketDetailContract.State())
    override val state: StateFlow<MarketDetailContract.State> = mutableState.asStateFlow()

    override fun event(event: MarketDetailContract.Event) = when (event) {
        is MarketDetailContract.Event.SetMarket -> setMarket(market = event.market)
        is MarketDetailContract.Event.OnFavoriteClick -> onFavoriteClick(market = event.market)
        is MarketDetailContract.Event.GetMarketChart -> getMarketChart(id = event.marketId)
        is MarketDetailContract.Event.GetMarketDetail -> getMarketDetail(id = event.marketId)
    }

    private fun getMarketDetail(id: String, isRefreshing: Boolean = false) {
        mutableBaseState.update { BaseContract.BaseState.OnLoading }
        getMarketDetailUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { detail ->
                        if (!isRefreshing) {
                            mutableBaseState.update {
                                BaseContract.BaseState.OnSuccess
                            }
                        } else {
                            mutableState.update {
                                MarketDetailContract.State(
                                    refreshing = false,
                                )
                            }
                        }
                        mutableState.update {
                            it.copy(marketDetail = detail, loading = false)
                        }
                    }
                }

                is Resource.Error -> {
                    mutableBaseState.update {
                        BaseContract.BaseState.OnError(
                            errors = result.error,
                        )
                    }
                }
            }
        }.catch { exception ->
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    Errors.ExceptionError(message = exception.message),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun setMarket(market: MarketModel?) {
        mutableState.update {
            it.copy(market = market)
        }
    }

    private fun onFavoriteClick(market: MarketModel?) {
        market?.let {
            viewModelScope.launch {
                onIO {
                    toggleFavoriteMarketListUseCase(market.toMarket())
                }
                toggleFavoriteState()
            }
        }
    }

    private fun toggleFavoriteState() {
        mutableState.update { state ->
            state.market?.let { market ->
                state.copy(
                    market = market.copy(isFavorite = !market.isFavorite),
                )
            } ?: state
        }
    }

    private fun getMarketChart(id: String, isRefreshing: Boolean = false) {
        mutableBaseState.update { BaseContract.BaseState.OnLoading }
        getMarketChartUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { chart ->
                        if (!isRefreshing) {
                            mutableBaseState.update {
                                BaseContract.BaseState.OnSuccess
                            }
                        } else {
                            mutableState.update {
                                MarketDetailContract.State(
                                    refreshing = false,
                                )
                            }
                        }
                        mutableState.update {
                            it.copy(marketChart = chart, loading = false)
                        }
                    }
                }

                is Resource.Error -> {
                    mutableBaseState.update {
                        BaseContract.BaseState.OnError(
                            errors = result.error,
                        )
                    }
                }
            }
        }.catch { exception ->
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    errors = Errors.ExceptionError(message = exception.message),
                )
            }
        }.launchIn(viewModelScope)
    }
}
