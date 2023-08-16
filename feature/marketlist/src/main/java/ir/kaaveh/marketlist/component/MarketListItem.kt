package ir.kaaveh.marketlist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.composenews.designsystem.component.MarketItem
import ir.kaaveh.domain.model.Market

@Composable
fun MarketListItem(
    modifier: Modifier,
    market: Market,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    with(market) {
        MarketItem(
            modifier = modifier,
            name = name,
            urlToImage = imageUrl,
            price = currentPrice.toString(),
            isFavorite = isFavorite,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick
        )
    }
}