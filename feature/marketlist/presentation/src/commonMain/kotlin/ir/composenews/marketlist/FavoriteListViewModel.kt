@file:Suppress("ktlint")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteListViewModel(
    private val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), FavoriteListContract {

    private val mutableState = MutableStateFlow(FavoriteListContract.State())
    override val state: StateFlow<FavoriteListContract.State> = mutableState.asStateFlow()

    private var favorites: List<MarketModel> = emptyList()
    private var observeJob: Job? = null

    override fun event(event: FavoriteListContract.Event) = when (event) {
        is FavoriteListContract.Event.OnGetFavoriteList -> getFavoriteList()
        is FavoriteListContract.Event.OnSortSelect -> {
            mutableState.update { it.copy(sort = event.sort) }
            publish()
        }
        is FavoriteListContract.Event.OnFavoriteClick -> onFavoriteClick(marketModel = event.market)
    }

    private fun getFavoriteList() {
        if (observeJob != null) return
        mutableState.update { it.copy(favoriteList = LoadableData.Loading) }
        observeJob = getFavoriteMarketListUseCase().onEach { list ->
            favorites = list.map { it.toMarketModel() }
            publish()
        }.launchIn(viewModelScope)
    }

    private fun publish() {
        mutableState.update {
            // A sort picked before the list was requested only sets the sort.
            if (it.favoriteList is LoadableData.Initial) it
            else it.copy(favoriteList = LoadableData.Loaded(favorites.sortedFor(it.sort).toPersistentList()))
        }
    }

    private fun onFavoriteClick(marketModel: MarketModel) {
        viewModelScope.launch {
            onIO {
                toggleFavoriteMarketListUseCase(marketModel.toMarket())
            }
        }
    }
}
