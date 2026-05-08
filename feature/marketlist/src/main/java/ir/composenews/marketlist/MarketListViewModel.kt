@file:Suppress("MaxLineLength", "TooGenericExceptionCaught", "ktlint")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketListViewModel(
    private val getPagedMarketListUseCase: GetPagedMarketListUseCase,
    private val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MarketListContract {

    private val favoriteIds = getFavoriteMarketListUseCase()
        .map { list -> list.mapTo(hashSetOf()) { it.id } }
        .distinctUntilChanged()

    // Ordering is load-bearing: cachedIn MUST stay upstream of combine.
    // Moving combine upstream would re-create the PagingSource on every favorite
    // toggle and silently re-fetch every page from the network.
    val pagedMarketList = getPagedMarketListUseCase()
        .map { pagingData -> pagingData.map { it.toMarketModel() } }
        .cachedIn(viewModelScope)
        .combine(favoriteIds) { paging, ids ->
            paging.map { model -> model.copy(isFavorite = model.id in ids) }
        }

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
        if (currentState.showFavoriteList == showFavoriteList && currentState.favoriteMarketList !is LoadableData.Initial) {
            return
        }
        mutableState.update {
            it.copy(showFavoriteList = showFavoriteList)
        }
        getData()
    }

    private fun getData() {
        dataJob?.cancel()
        if (mutableState.value.showFavoriteList) {
            mutableState.update { it.copy(favoriteMarketList = LoadableData.Loading) }
            dataJob = getFavoriteMarketList()
        }
    }

    private fun getFavoriteMarketList(): Job = getFavoriteMarketListUseCase().onEach { newList ->
        val marketList = newList.map { it.toMarketModel() }.toPersistentList()
        mutableState.update {
            it.copy(favoriteMarketList = LoadableData.Loaded(data = marketList))
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
