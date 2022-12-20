package ir.kaaveh.newsdetail

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import ir.kaaveh.designsystem.collectInLaunchedEffect
import ir.kaaveh.designsystem.component.FavoriteIcon
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.useWithEffect
import ir.kaaveh.domain.model.News
import ir.kaaveh.newsdetail.preview_provider.NewsDetailStateProvider

@Composable
fun NewsDetailRoute(
    news: News?,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val (state, effect, event) = useWithEffect(viewModel = viewModel)
    val activity = LocalContext.current as? Activity

    LaunchedEffect(key1 = news) {
        event.invoke(NewsDetailContract.Event.SetNews(news = news))
    }

    effect.collectInLaunchedEffect {
        when (it) {
            NewsDetailContract.Effect.OnBackPressed -> {
                activity?.onBackPressed()
            }
        }
    }

    NewsDetailScreen(
        newsDetailState = state,
        onFavoriteClick = {
            event.invoke(NewsDetailContract.Event.OnFavoriteClick(news = it))
        },
    )
}

@Composable
fun NewsDetailScreen(
    newsDetailState: NewsDetailContract.State,
    onFavoriteClick: (news: News?) -> Unit,
) {
    val webViewState = rememberWebViewState(newsDetailState.news?.url ?: "")

    Box(modifier = Modifier.fillMaxSize()) {
        WebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
            captureBackPresses = false,
        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {},
            backgroundColor = Color.White,
        ) {
            FavoriteIcon(isFavorite = newsDetailState.news?.isFavorite ?: false) {
                onFavoriteClick(newsDetailState.news)
            }
        }
    }

}

@ThemePreviews
@Composable
private fun NewsDetailScreenPrev(
    @PreviewParameter(NewsDetailStateProvider::class)
    newsDetailState: NewsDetailContract.State
) {
    NewsDetailScreen(newsDetailState = newsDetailState, onFavoriteClick = {})
}