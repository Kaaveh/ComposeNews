package ir.kaaveh.favoritenews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.favoritenews.component.FavoriteNewsItem

@Composable
fun FavoriteNewsScreen(
    viewModel: FavoriteNewsViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (arg: String) -> Unit,
) {

    val state = viewModel.state.value

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.news) { news ->
                FavoriteNewsItem(
                    news = news,
                    onItemClick = {
                        onNavigateToDetailScreen(news.url)
                    },
                    onFavoriteClick = {
                        viewModel.onFavoriteClick(news)
                    }
                )
            }
        }
    }

}