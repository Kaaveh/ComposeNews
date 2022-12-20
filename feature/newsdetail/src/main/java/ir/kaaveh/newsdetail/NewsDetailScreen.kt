package ir.kaaveh.newsdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import ir.kaaveh.designsystem.component.FavoriteIcon
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.domain.model.News
import ir.kaaveh.newsdetail.preview_provider.NewsProvider

@Composable
fun NewsDetailRoute(
    news: News?,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    NewsDetailScreen(
        news = news,
        onFavoriteClick = {
            viewModel.onFavoriteClick(news)
        },
    )
}

@Composable
fun NewsDetailScreen(
    news: News?,
    onFavoriteClick: (news: News?) -> Unit,
) {
    val webViewState = rememberWebViewState(news?.url ?: "")
    var favorite by remember { mutableStateOf(news?.isFavorite ?: false) }

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
            onClick = {
                onFavoriteClick(news)
                favorite = !favorite
            },
            backgroundColor = Color.White,
        ) {
            FavoriteIcon(isFavorite = favorite) {}
        }
    }

}

@ThemePreviews
@Composable
private fun NewsDetailScreenPrev(
    @PreviewParameter(NewsProvider::class)
    news: News
) {
    NewsDetailScreen(news = news, onFavoriteClick = {})
}