package ir.kaaveh.newslist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseContract
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.use_case.GetFavoriteMarketListUseCase
import ir.kaaveh.domain.use_case.GetMarketListUseCase
import ir.kaaveh.domain.use_case.ToggleFavoriteMarketListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketListViewModel @Inject constructor(
    private val getMarketListUseCase: GetMarketListUseCase,
    private val getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase,
    private val toggleFavoriteMarketListUseCase: ToggleFavoriteMarketListUseCase,
) : BaseViewModel(), MarketListContract {

    private val mutableState = MutableStateFlow(MarketListContract.State())
    override val state: StateFlow<MarketListContract.State> = mutableState.asStateFlow()

    override fun event(event: MarketListContract.Event) = when (event) {
        is MarketListContract.Event.OnSetShowFavoriteList -> onSetShowFavoriteList(showFavoriteList = event.showFavoriteList)
        MarketListContract.Event.OnGetMarketList -> getData()
        is MarketListContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.market)
        MarketListContract.Event.OnRefresh -> getData(isRefreshing = true)
    }

    private fun onSetShowFavoriteList(showFavoriteList: Boolean) {
        mutableState.update {
            it.copy(showFavoriteList = showFavoriteList)
        }
    }

    private fun getData(isRefreshing: Boolean = false) {
        if (isRefreshing)
            mutableState.update {
                MarketListContract.State(
                    refreshing = true,
                )
            }
        viewModelScope.launch {
            if (mutableState.value.showFavoriteList)
                getFavoriteMarketList()
            else
                getMarketList()
        }
    }

    private suspend fun getMarketList() = getMarketListUseCase()
        .catch { exception ->
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    errorMessage = exception.localizedMessage ?: "An unexpected error occurred."
                )
            }
        }
        .onEach { result ->
            mutableState.update {
                MarketListContract.State(marketList = result)
            }
        }
        .launchIn(viewModelScope)

    private fun getFavoriteMarketList() = getFavoriteMarketListUseCase().onEach { newList ->
        mutableState.update {
            it.copy(marketList = newList)
        }
    }.launchIn(viewModelScope)

    // TODO: remove these comments
//    private fun getNewsList(isRefreshing: Boolean = false) = getNewsUseCase().onEach { result ->
//        when (result) {
//            is Resource.Loading -> {
//                if (!isRefreshing)
//                    mutableBaseState.update {
//                        BaseContract.BaseState.OnLoading
//                    }
//            }
//            is Resource.Success -> {
//                if (!isRefreshing)
//                    mutableBaseState.update {
//                        BaseContract.BaseState.OnSuccess
//                    }
//                else
//                    mutableState.update {
//                        NewsListContract.State(
//                            refreshing = false,
//                        )
//                    }
//                mutableState.update {
//                    NewsListContract.State(
//                        news = result.data ?: listOf()
//                    )
//                }
//            }
//            is Resource.Error -> {
//                mutableBaseState.update {
//                    BaseContract.BaseState.OnError(
//                        errorMessage = result.exception?.localizedMessage
//                            ?: "An unexpected error occurred."
//                    )
//                }
//            }
//        }
//    }.launchIn(viewModelScope)

    private fun onFavoriteClick(news: Market) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavoriteMarketListUseCase(news)
        }
    }

}