package ir.kaaveh.favoritenews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteNewsScreen(
    viewModel: FavoriteNewsViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.news) { news ->
                NewsListItem(
                    news = news,
                    onItemClick = {
                        // TODO: Navigate to detail screen
                    },
                    onFavoriteClick = {
                        viewModel.onFavoriteClick(news)
                    }
                )
            }
        }
    }

}