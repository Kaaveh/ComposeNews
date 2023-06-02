package ir.kaaveh.newslist.component

import androidx.compose.runtime.Composable
import ir.kaaveh.domain.model.Market

@Composable
fun MarketListItem(
    market: Market,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    with(market) {
//        NewsItem(
//            title = title,
//            urlToImage = urlToImage,
//            description = description,
//            source = source,
//            publishedAt = publishedAt,
//            isFavorite = isFavorite,
//            onItemClick = onItemClick,
//            onFavoriteClick = onFavoriteClick
//        )
    }
}