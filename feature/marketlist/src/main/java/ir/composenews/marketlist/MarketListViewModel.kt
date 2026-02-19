@file:Suppress("MaxLineLength", "TooGenericExceptionCaught", "ktlint")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
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
    private var dataJob: Job? = null

    override fun event(event: MarketListContract.Event) = when (event) {
        is MarketListContract.Event.OnGetMarketList -> getData()
        is MarketListContract.Event.OnFavoriteClick -> onFavoriteClick(marketModel = event.market)
        is MarketListContract.Event.OnSetShowFavoriteList -> onSetShowFavoriteList(
            showFavoriteList = event.showFavoriteList,
        )
    }

    private fun onSetShowFavoriteList(showFavoriteList: Boolean) {
        val currentState = mutableState.value
        if (currentState.showFavoriteList == showFavoriteList && currentState.marketList !is LoadableData.Initial) {
            return
        }
        mutableState.update {
            it.copy(showFavoriteList = showFavoriteList)
        }
        getData()
    }

    private fun getData() {
        dataJob?.cancel()
        mutableState.update {
            it.copy(marketList = LoadableData.Loading)
        }
        dataJob = if (mutableState.value.showFavoriteList) {
            getFavoriteMarketList()
        } else {
            getMarketList()
        }
    }

    private fun getMarketList(): Job = viewModelScope.launch {
        getMarketListUseCase()
            .onEach { newList ->
                val marketList = newList.map { it.toMarketModel() }.toPersistentList()
                mutableState.update {
                    it.copy(marketList = LoadableData.Loaded(data = marketList))
                }
            }
            .catch { exception ->
                mutableState.update {
                    it.copy(
                        marketList = LoadableData.Error(
                            error = Errors.ExceptionError(message = exception.message),
                        ),
                    )
                }
            }
            .collect {}
    }

    private fun getFavoriteMarketList(): Job = getFavoriteMarketListUseCase().onEach { newList ->
        val marketList = newList.map { it.toMarketModel() }.toPersistentList()

        mutableState.update {
            it.copy(marketList = LoadableData.Loaded(data = marketList))
        }
    }.launchIn(viewModelScope)

    private fun onFavoriteClick(marketModel: MarketModel) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(marketModel.toMarket())
            }
        }
    }
}
