@file:Suppress("ktlint")

package ir.composenews.marketlist

import androidx.lifecycle.viewModelScope
import ir.composenews.base.BaseViewModel
import ir.composenews.base.LoadableData
import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SECOND_MILLIS = 1_000L

class MarketListViewModel(
    private val getMarketListUseCase: GetMarketListUseCase,
    private val syncMarketListUseCase: SyncMarketListUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(dispatcherProvider), MarketListContract {

    private val mutableState = MutableStateFlow(MarketListContract.State())
    override val state: StateFlow<MarketListContract.State> = mutableState.asStateFlow()

    /** Every stored market, before the filter and sort. */
    private var markets: List<MarketModel> = emptyList()
    private var hasSynced = false
    private var observeJob: Job? = null
    private var syncJob: Job? = null
    private var countdownJob: Job? = null

    override fun event(event: MarketListContract.Event) = when (event) {
        is MarketListContract.Event.OnGetMarketList -> getMarketList()
        is MarketListContract.Event.OnRefresh -> sync()
        is MarketListContract.Event.OnFilterSelect -> {
            mutableState.update { it.copy(filter = event.filter) }
            publish()
        }
        is MarketListContract.Event.OnSortSelect -> {
            mutableState.update { it.copy(sort = event.sort) }
            publish()
        }
        is MarketListContract.Event.OnFavoriteClick -> onFavoriteClick(marketModel = event.market)
        is MarketListContract.Event.OnSyncErrorShown -> mutableState.update { it.copy(syncError = null) }
    }

    // The ViewModel outlives the screen, so coming back to it must not sync again.
    private fun getMarketList() {
        if (observeJob != null) return
        observeJob = storedMarkets()
            .onEach { list ->
                markets = list
                publish()
            }.launchIn(viewModelScope)
        sync()
    }

    private fun storedMarkets() = getMarketListUseCase()
        .map { list -> list.map { it.toMarketModel() } }
        .flowOn(dispatcherProvider.bg)

    private fun sync() {
        if (syncJob?.isActive == true || state.value.retryInSeconds > 0) return
        mutableState.update {
            it.copy(
                isRefreshing = true,
                syncError = null,
                marketList = if (it.marketList is LoadableData.Loaded) it.marketList else LoadableData.Loading,
            )
        }
        syncJob = viewModelScope.launch {
            val result = onIO { syncMarketListUseCase() }
            mutableState.update { it.copy(isRefreshing = false) }
            when (result) {
                is Resource.Success -> {
                    // Room tells the observer about the write a moment later; read it now so a first
                    // install doesn't flash an empty list.
                    markets = storedMarkets().first()
                    hasSynced = true
                    publish()
                }

                is Resource.Error -> {
                    // Stored markets stay on screen; the error only replaces an empty list.
                    mutableState.update {
                        if (markets.isEmpty()) it.copy(marketList = LoadableData.Error(result.error))
                        else it.copy(syncError = result.error)
                    }
                    (result.error as? Errors.RateLimited)?.let { startCountdown(it.retryAfterSeconds) }
                }
            }
        }
    }

    private fun startCountdown(seconds: Int) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (left in seconds downTo 0) {
                mutableState.update { it.copy(retryInSeconds = left) }
                if (left > 0) delay(SECOND_MILLIS)
            }
        }
    }

    private fun publish() {
        // An empty table before the first sync answers means "still loading", not "no markets".
        if (markets.isEmpty() && !hasSynced) return
        mutableState.update {
            it.copy(
                marketList = LoadableData.Loaded(markets.filteredAndSorted(it.filter, it.sort).toPersistentList()),
                topMover = markets.topMover(),
            )
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
