package ir.kaaveh.newslist.component

import androidx.compose.runtime.Composable
import ir.kaaveh.designsystem.component.NewsItem
import ir.kaaveh.domain.model.Market

@Composable
fun NewsListItem(
    news: Market,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    with(news) {
        NewsItem(
            title = title,
            urlToImage = urlToImage,
            description = description,
            source = source,
            publishedAt = publishedAt,
            isFavorite = isFavorite,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick
        )
    }
}