package ir.kaaveh.newslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (news: News) -> Unit,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
        event.invoke(NewsListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
        event.invoke(NewsListContract.Event.OnGetNewsList)
    }

    NewsListScreen(
        newsListState = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = { news ->
            event.invoke(NewsListContract.Event.OnFavoriteClick(news = news))
        },
        onRefresh = {
            event.invoke(NewsListContract.Event.OnRefresh)
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NewsListScreen(
    newsListState: NewsListContract.State,
    onNavigateToDetailScreen: (news: News) -> Unit,
    onFavoriteClick: (news: News) -> Unit,
    onRefresh: () -> Unit,
) {
    val refreshState =
        rememberPullRefreshState(refreshing = newsListState.refreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState)
    ) {
        AnimatedVisibility(
            visible = !newsListState.refreshing,
            enter = fadeIn(),
            exit = fadeOut(),
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
        PullRefreshIndicator(
            newsListState.refreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter)
        )
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
        onRefresh = {},
    )
}