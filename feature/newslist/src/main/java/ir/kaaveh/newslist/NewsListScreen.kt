package ir.kaaveh.newslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.domain.model.News
import ir.kaaveh.newslist.component.NewsListItem
import ir.kaaveh.newslist.preview_provider.NewsListStateProvider

@Composable
fun NewsListRoute(
    viewModel: NewsListViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (news: News) -> Unit,
    onFavoriteClick: (news: News) -> Unit,
) {
    val state = viewModel.state.value

    NewsListScreen(
        newsListState = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
    )
}

@Composable
fun NewsListScreen(
    newsListState: NewsListState,
    onNavigateToDetailScreen: (news: News) -> Unit,
    onFavoriteClick: (news: News) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            newsListState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            newsListState.error.isNotBlank() -> {
                Text(
                    text = newsListState.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            else -> {
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
        }
    }
}

@ThemePreviews
@Composable
private fun NewsListScreenPrev(
    @PreviewParameter(NewsListStateProvider::class)
    newsListState: NewsListState
) {
    NewsListScreen(
        newsListState = newsListState,
        onNavigateToDetailScreen = {},
        onFavoriteClick = {},
    )
}