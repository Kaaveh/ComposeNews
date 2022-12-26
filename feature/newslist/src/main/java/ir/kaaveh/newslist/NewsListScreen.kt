package ir.kaaveh.newslist

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
import ir.kaaveh.newslist.component.NewsListItem
import ir.kaaveh.newslist.preview_provider.NewsListStateProvider

@Composable
fun NewsListRoute(
    viewModel: NewsListViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (news: News) -> Unit,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
    }

    NewsListScreen(
        newsListState = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = { news ->
            event.invoke(NewsListContract.Event.OnFavoriteClick(news = news))
        },
    )
}

@Composable
private fun NewsListScreen(
    newsListState: NewsListContract.State,
    onNavigateToDetailScreen: (news: News) -> Unit,
    onFavoriteClick: (news: News) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(newsListState.news) { news ->
            NewsListItem(
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

@ThemePreviews
@Composable
private fun NewsListScreenPrev(
    @PreviewParameter(NewsListStateProvider::class)
    newsListState: NewsListContract.State
) {
    NewsListScreen(
        newsListState = newsListState,
        onNavigateToDetailScreen = {},
        onFavoriteClick = {},
    )
}