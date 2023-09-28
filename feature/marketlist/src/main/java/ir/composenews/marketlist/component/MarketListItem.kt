package ir.composenews.marketlist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.composenews.designsystem.component.MarketItem
import ir.composenews.domain.model.Market

@Composable
fun MarketListItem(
    modifier: Modifier,
    market: Market,
    showFavoriteList: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    with(market) {
        MarketItem(
            modifier = modifier,
            name = name,
            symbol = symbol,
            urlToImage = imageUrl,
            price = currentPrice.toString(),
            priceChangePercentage24h = priceChangePercentage24h.toString(),
            isFavorite = isFavorite,
            showFavoriteList = showFavoriteList,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick,
        )
    }
}
