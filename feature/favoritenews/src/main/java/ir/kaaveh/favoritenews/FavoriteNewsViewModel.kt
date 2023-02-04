package ir.kaaveh.favoritenews

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.use_case.ToggleFavoriteNewsUseCase
import ir.kaaveh.domain.use_case.GetFavoriteNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNewsViewModel @Inject constructor(
    private val toggleFavoriteNewsUseCase: ToggleFavoriteNewsUseCase,
    private val getFavoriteNewsUseCase: GetFavoriteNewsUseCase,
) : BaseViewModel(), FavoriteNewsContract {

    private val mutableState = MutableStateFlow(FavoriteNewsContract.State())
    override val state: StateFlow<FavoriteNewsContract.State> = mutableState.asStateFlow()

    init {
        getFavoriteNews()
    }

    override fun event(event: FavoriteNewsContract.Event) = when (event) {
        is FavoriteNewsContract.Event.OnFavoriteClick -> onFavoriteClick(news = event.news)
    }

    private fun getFavoriteNews() = getFavoriteNewsUseCase().onEach {newList ->
        mutableState.update {
            it.copy(news = newList)
        }
    }.launchIn(viewModelScope)

    private fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavoriteNewsUseCase(news)
        }
    }

}