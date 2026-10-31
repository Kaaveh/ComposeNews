@file:Suppress("LongParameterList", "ktlint")

package ir.composenews.marketlist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import ir.composenews.designsystem.component.MarketRow
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.uimarket.model.MarketModel

/** A null [onFavoriteClick] hides the heart; [onRemove] is then the screen-reader action for the swipe. */
@Composable
fun MarketListItem(
    market: MarketModel,
    shape: Shape,
    onItemClick: () -> Unit,
    onFavoriteClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
) {
    with(market) {
        MarketRow(
            id = id,
            name = name,
            symbol = symbol,
            imageUrl = imageUrl,
            price = formatUsdPrice(currentPrice),
            change = priceChangePercentage24h,
            sparkline = sparkline7d,
            isTrendingUp7d = isTrendingUp7d,
            isFavorite = isFavorite,
            onClick = onItemClick,
            onFavoriteClick = onFavoriteClick,
            modifier = modifier,
            shape = shape,
            onRemove = onRemove,
        )
    }
}
