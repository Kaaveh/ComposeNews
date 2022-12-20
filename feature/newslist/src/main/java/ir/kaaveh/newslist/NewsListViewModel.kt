package ir.kaaveh.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.model.Resource
import ir.kaaveh.domain.use_case.AddFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.GetFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.GetNewsUseCase
import ir.kaaveh.domain.use_case.RemoveFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val addFavoriteNewsUseCase: AddFavoriteNewsUseCase,
    private val removeFavoriteNewsUseCase: RemoveFavoriteNewsUseCase,
    private val getFavoriteNewsUseCase: GetFavoriteNewsUseCase,
) : ViewModel(), NewsListContract {

    private val mutableState = MutableStateFlow(NewsListContract.State())
    override val state: StateFlow<NewsListContract.State> = mutableState.asStateFlow()

    init {
        getNewsList()
        getFavoriteNews()
    }

    override fun event(event: NewsListContract.Event) = when (event) {
        is NewsListContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.news)
    }

    private fun getNewsList() = getNewsUseCase().onEach { result ->
        when (result) {
            is Resource.Loading -> {
                mutableState.update {
                    NewsListContract.State(isLoading = true)
                }
            }
            is Resource.Success -> {
                mutableState.update {
                    NewsListContract.State(
                        news = result.data ?: listOf()
                    )
                }
            }
            is Resource.Error -> {
                mutableState.update {
                    NewsListContract.State(
                        error = result.exception?.localizedMessage
                            ?: "An unexpected error occurred."
                    )
                }
            }
        }
    }.launchIn(viewModelScope)

    private fun getFavoriteNews() = getFavoriteNewsUseCase().onEach { favoriteList ->
        val updatedList = mutableState.value.news.map { news ->
            val temp = favoriteList.find { it.title == news.title }
            temp?.copy(isFavorite = true) ?: news.copy(isFavorite = false)
        }
        mutableState.update {
            it.copy(news = updatedList)
        }
    }.launchIn(viewModelScope)

    private fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!news.isFavorite)
                addFavoriteNewsUseCase(news)
            else
                removeFavoriteNewsUseCase(news)
        }
    }

}