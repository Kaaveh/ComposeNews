package ir.kaaveh.favoritenews

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.AddFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.GetFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.RemoveFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNewsViewModel @Inject constructor(
    private val addFavoriteNewsUseCase: AddFavoriteNewsUseCase,
    private val removeFavoriteNewsUseCase: RemoveFavoriteNewsUseCase,
    private val getFavoriteNewsUseCase: GetFavoriteNewsUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(FavoriteNewsState())
    val state: State<FavoriteNewsState> = _state

    init {
        getFavoriteNews()
    }

    private fun getFavoriteNews() = getFavoriteNewsUseCase().onEach {
        _state.value = _state.value.copy(news = it)
    }.launchIn(viewModelScope)

    fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!news.isFavorite)
                addFavoriteNewsUseCase(news)
            else
                removeFavoriteNewsUseCase(news)
        }
    }

}