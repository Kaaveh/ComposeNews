package ir.composenews.marketlist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.composenews.designsystem.component.MarketItem
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MarketListItem(
    modifier: Modifier,
    market: MarketModel,
    showFavoriteList: Boolean,
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
            showFavoriteList = showFavoriteList,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick,
        )
    }
}
