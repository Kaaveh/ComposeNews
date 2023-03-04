package ir.kaaveh.newslist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseContract
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.GetNewsUseCase
import ir.kaaveh.domain.use_case.ToggleFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val toggleFavoriteNewsUseCase: ToggleFavoriteNewsUseCase,
) : BaseViewModel(), NewsListContract {

    private val mutableState = MutableStateFlow(NewsListContract.State())
    override val state: StateFlow<NewsListContract.State> = mutableState.asStateFlow()

    init {
        getData()
    }

    override fun event(event: NewsListContract.Event) = when (event) {
        is NewsListContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.news)
        NewsListContract.Event.OnRefresh -> getData(isRefreshing = true)
    }

    private fun getData(isRefreshing: Boolean = false) {
        if (isRefreshing)
            mutableState.update {
                NewsListContract.State(
                    refreshing = true,
                )
            }
        viewModelScope.launch {
            getNewsList()
        }
    }

    private suspend fun getNewsList() = getNewsUseCase()
        .catch { exception ->
            mutableBaseState.update {
                BaseContract.BaseState.OnError(
                    errorMessage = exception.localizedMessage ?: "An unexpected error occurred."
                )
            }
        }
        .onEach { result ->
            mutableState.update {
                NewsListContract.State(news = result)
            }
        }
        .launchIn(viewModelScope)

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

    private fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavoriteNewsUseCase(news)
        }
    }

}