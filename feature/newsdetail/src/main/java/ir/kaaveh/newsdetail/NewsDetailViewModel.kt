package ir.kaaveh.newsdetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.SetFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val setFavoriteNewsUseCase: SetFavoriteNewsUseCase
) : BaseViewModel(), NewsDetailContract {

    private val mutableState = MutableStateFlow(NewsDetailContract.State())
    override val state: StateFlow<NewsDetailContract.State> = mutableState.asStateFlow()

    override fun event(event: NewsDetailContract.Event) = when (event) {
        is NewsDetailContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.news)
        is NewsDetailContract.Event.SetNews -> setNewsState(news = event.news)
    }

    private fun onFavoriteClick(news: News?) {
        news?.let {
            viewModelScope.launch(Dispatchers.IO) {
                setFavoriteNewsUseCase(news)
                toggleFavoriteState()
            }
        }
    }

    private fun setNewsState(news: News?) {
        mutableState.update {
            it.copy(news = news)
        }
    }

    private fun toggleFavoriteState() {
        mutableState.update { state ->
            state.news?.let { news ->
                state.copy(
                    news = news.copy(isFavorite = !news.isFavorite)
                )
            } ?: state
        }
    }

}