package ir.kaaveh.marketdetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.use_case.ToggleFavoriteMarketListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketDetailViewModel @Inject constructor(
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase
) : BaseViewModel(), MarketDetailContract {

    private val mutableState = MutableStateFlow(MarketDetailContract.State())
    override val state: StateFlow<MarketDetailContract.State> = mutableState.asStateFlow()

    override fun event(event: MarketDetailContract.Event) = when (event) {
        is MarketDetailContract.Event.SetMarket -> setMarket(market = event.market)
        is MarketDetailContract.Event.OnFavoriteClick -> onFavoriteClick(market = event.market)
    }

    private fun setMarket(market: Market?) {
        mutableState.update {
            it.copy(market = market)
        }
    }

    private fun onFavoriteClick(market: Market?) {
        market?.let {
            viewModelScope.launch(Dispatchers.IO) {
                toggleFavoriteMarketListUseCase(market)
                toggleFavoriteState()
            }
        }
    }

    private fun toggleFavoriteState() {
        mutableState.update { state ->
            state.market?.let { market ->
                state.copy(
                    market = market.copy(isFavorite = !market.isFavorite)
                )
            } ?: state
        }
    }

}