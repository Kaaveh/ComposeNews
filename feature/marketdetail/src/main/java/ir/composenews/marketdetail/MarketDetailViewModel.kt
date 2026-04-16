@file:Suppress("ImportOrdering", "ktlint")

package ir.composenews.marketdetail

import androidx.lifecycle.viewModelScope
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
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

class MarketDetailViewModel(
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

    private fun getMarketDetail(id: String) {
        mutableState.update {
            it.copy(marketDetail = LoadableData.Loading)
        }

        getMarketDetailUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { detail ->
                        mutableState.update {
                            it.copy(marketDetail = LoadableData.Loaded(data = detail))
                        }
                    }
                }

                is Resource.Error -> {
                    mutableState.update {
                        it.copy(
                            marketDetail = LoadableData.Error(error = result.error),
                        )
                    }
                }
            }
        }.catch { exception ->
            mutableState.update {
                it.copy(
                    marketDetail = LoadableData.Error(error = Errors.ExceptionError(message = exception.message)),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun setMarket(market: MarketModel) {
        mutableState.update {
            it.copy(market = LoadableData.Loaded(market))
        }
    }

    private fun getMarketChart(id: String) {
        mutableState.update {
            it.copy(marketChart = LoadableData.Loading)
        }

        getMarketChartUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { chart ->
                        mutableState.update {
                            it.copy(marketChart = LoadableData.Loaded(data = chart))
                        }
                    }
                }

                is Resource.Error -> {
                    mutableState.update {
                        it.copy(
                            marketChart = LoadableData.Error(error = result.error),
                        )
                    }
                }
            }
        }.catch { exception ->
            mutableState.update {
                it.copy(
                    marketChart = LoadableData.Error(error = Errors.ExceptionError(message = exception.message)),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun onFavoriteClick(market: MarketModel) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(market.toMarket())
            }
            toggleFavoriteState()
        }
    }

    private fun toggleFavoriteState() {
        val market = (mutableState.value.market as LoadableData.Loaded).data
        val newMarket = LoadableData.Loaded(market.copy(isFavorite = !market.isFavorite))
        mutableState.update { it.copy(market = newMarket) }
    }
}
