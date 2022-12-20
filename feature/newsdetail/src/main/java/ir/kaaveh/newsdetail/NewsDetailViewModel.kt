package ir.kaaveh.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.AddFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.RemoveFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val addFavoriteNewsUseCase: AddFavoriteNewsUseCase,
    private val removeFavoriteNewsUseCase: RemoveFavoriteNewsUseCase,
) : ViewModel(), NewsDetailContract {

    private val mutableState = MutableStateFlow(NewsDetailContract.State())
    override val state: StateFlow<NewsDetailContract.State> = mutableState.asStateFlow()

    private val effectChannel = Channel<NewsDetailContract.Effect>(Channel.UNLIMITED)
    override val effect: Flow<NewsDetailContract.Effect> = effectChannel.receiveAsFlow()

    override fun event(event: NewsDetailContract.Event) = when (event) {
        is NewsDetailContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.news)
        is NewsDetailContract.Event.SetNews -> setNewsState(news = event.news)
        NewsDetailContract.Event.OnBackPressed -> onBackPressed()
    }

    private fun onFavoriteClick(news: News?) {
        news?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (!news.isFavorite) {
                    addFavoriteNewsUseCase(news)
                } else {
                    removeFavoriteNewsUseCase(news)
                }
                toggleFavoriteState()
            }
        }
    }

    private fun setNewsState(news: News?) {
        mutableState.update {
            it.copy(news = news)
        }
    }

    private fun onBackPressed() {
        effectChannel.trySend(NewsDetailContract.Effect.OnBackPressed)
    }

    private fun toggleFavoriteState() {
        mutableState.update { state ->
            state.news?.let {news ->
                state.copy(
                    news = news.copy(isFavorite = !news.isFavorite)
                )
            } ?: state
        }
    }

}