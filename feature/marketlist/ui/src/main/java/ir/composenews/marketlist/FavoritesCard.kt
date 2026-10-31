@file:Suppress("MagicNumber", "UnusedPrivateMember", "ktlint")

package ir.composenews.marketlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import ir.composenews.core.ui.marketList
import ir.composenews.core.ui.use
import ir.composenews.designsystem.component.MarketLogo
import ir.composenews.designsystem.component.TrendChange
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import java.util.Locale

private const val MAX_FAVORITES = 5

/** The desktop extra pane's favorites: a few saved markets besides the one open, and `See all`. */
@Composable
fun FavoritesCardRoute(
    selectedMarketId: String?,
    onMarketClick: (market: MarketModel) -> Unit,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (state, event) = use(viewModel = koinViewModel<FavoriteListViewModel>())
    LaunchedEffect(key1 = Unit) {
        event.invoke(FavoriteListContract.Event.OnGetFavoriteList)
    }
    FavoritesCard(
        favorites = state.favoriteList.data,
        selectedMarketId = selectedMarketId,
        onMarketClick = onMarketClick,
        onSeeAll = onSeeAll,
        modifier = modifier,
    )
}

/** [favorites] is null while they load, so `No favorites yet` doesn't flash first. */
@Composable
fun FavoritesCard(
    favorites: List<MarketModel>?,
    selectedMarketId: String?,
    onMarketClick: (market: MarketModel) -> Unit,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { heading() },
                )
                TextButton(onClick = onSeeAll) { Text("See all") }
            }
            val others = favorites?.filter { it.id != selectedMarketId }
            when {
                favorites == null || others == null -> Unit
                others.isEmpty() -> Text(
                    // The open market can be the only favorite.
                    text = if (favorites.isEmpty()) "No favorites yet" else "No other favorites",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                else -> others.take(MAX_FAVORITES).forEach { market ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onMarketClick(market) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 36.dp, shared = false)
                        Text(
                            text = market.symbol.uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            modifier = Modifier.weight(1f),
                        )
                        TrendChange(change = market.priceChangePercentage24h)
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun FavoritesCardPreview() {
    ComposeNewsTheme {
        Surface {
            FavoritesCard(
                favorites = marketList.take(4).map { it.toMarketModel().copy(id = it.name, isFavorite = true) },
                selectedMarketId = null,
                onMarketClick = {},
                onSeeAll = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
