@file:Suppress("ImportOrdering", "ktlint")

package ir.composenews.marketdetail

import androidx.lifecycle.viewModelScope
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketDetailViewModel(
    private val getMarketChartUseCase: GetMarketChartUseCase,
    private val getMarketDetailUseCase: GetMarketDetailUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    private val getMarketByIdUseCase: GetMarketByIdUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MarketDetailContract {

    private var favoriteObserverJob: Job? = null

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
        observeFavoriteState(market.id)
    }

    private fun observeFavoriteState(id: String) {
        favoriteObserverJob?.cancel()
        favoriteObserverJob = getMarketByIdUseCase(id)
            .distinctUntilChanged()
            .onEach { dbMarket ->
                if (dbMarket == null) return@onEach
                mutableState.update { state ->
                    val current = state.market
                    if (current is LoadableData.Loaded && current.data.isFavorite != dbMarket.isFavorite) {
                        state.copy(
                            market = LoadableData.Loaded(
                                current.data.copy(isFavorite = dbMarket.isFavorite),
                            ),
                        )
                    } else {
                        state
                    }
                }
            }.launchIn(viewModelScope)
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
        }
    }
}
