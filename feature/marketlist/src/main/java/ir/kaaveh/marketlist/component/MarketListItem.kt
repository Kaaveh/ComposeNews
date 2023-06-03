package ir.kaaveh.marketlist.component

import androidx.compose.runtime.Composable
import ir.kaaveh.designsystem.component.MarketItem
import ir.kaaveh.domain.model.Market

@Composable
fun MarketListItem(
    market: Market,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    with(market) {
        MarketItem(
            name = name,
            urlToImage = imageUrl,
            price = currentPrice.toString(),
            isFavorite = isFavorite,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick
        )
    }
}