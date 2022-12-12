package ir.kaaveh.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.AddFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.RemoveFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val addFavoriteNewsUseCase: AddFavoriteNewsUseCase,
    private val removeFavoriteNewsUseCase: RemoveFavoriteNewsUseCase,
) : ViewModel() {

    fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!news.isFavorite) {
                addFavoriteNewsUseCase(news)
            } else {
                removeFavoriteNewsUseCase(news)
            }
        }
    }

}