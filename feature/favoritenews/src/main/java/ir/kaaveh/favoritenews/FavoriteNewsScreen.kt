package ir.kaaveh.favoritenews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.use
import ir.kaaveh.domain.model.News
import ir.kaaveh.favoritenews.component.FavoriteNewsItem
import ir.kaaveh.favoritenews.preview_provider.FavoriteNewsStateProvider

@Composable
fun FavoriteNewsRoute(
    viewModel: FavoriteNewsViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (news: News) -> Unit,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
    }

    FavoriteNewsScreen(
        favoriteNewsState = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = { news ->
            event.invoke(FavoriteNewsContract.Event.OnFavoriteClick(news = news))
        }
    )
}

@Composable
private fun FavoriteNewsScreen(
    favoriteNewsState: FavoriteNewsContract.State,
    onNavigateToDetailScreen: (news: News) -> Unit,
    onFavoriteClick: (news: News) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(favoriteNewsState.news) { news ->
                FavoriteNewsItem(
                    news = news,
                    onItemClick = {
                        onNavigateToDetailScreen(news)
                    },
                    onFavoriteClick = {
                        onFavoriteClick(news)
                    }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun FavoriteNewsScreenPrev(
    @PreviewParameter(FavoriteNewsStateProvider::class)
    favoriteNewsState: FavoriteNewsContract.State
) {
    FavoriteNewsScreen(
        favoriteNewsState = favoriteNewsState,
        onNavigateToDetailScreen = {},
        onFavoriteClick = {},
    )
}