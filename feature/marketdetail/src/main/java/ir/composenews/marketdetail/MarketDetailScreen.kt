package ir.composenews.marketdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import ir.composenews.base.BaseRoute
import ir.composenews.base.use
import ir.composenews.designsystem.component.FavoriteIcon
import ir.composenews.designsystem.component.QuadLineChart
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.domain.model.Market
import ir.composenews.marketdetail.preview_provider.MarketDetailStateProvider

@Composable
fun MarketDetailRoute(
    market: Market?,
    viewModel: MarketDetailViewModel = hiltViewModel(),
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
        market?.let {
            event.invoke(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        }
    }

    BaseRoute(baseViewModel = viewModel) {
        MarketDetailScreen(
            marketDetailState = state,
            onFavoriteClick = {
                event.invoke(MarketDetailContract.Event.OnFavoriteClick(market = it))
            },
        )
    }
}

@Composable
private fun MarketDetailScreen(
    marketDetailState: MarketDetailContract.State,
    onFavoriteClick: (market: Market?) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = marketDetailState.market?.imageUrl),
                        contentDescription = marketDetailState.market?.name,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                    )
                    Column(
                        modifier = Modifier.weight(1F),
                    ) {
                        Text(
                            text = marketDetailState.market?.name ?: "--",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = "${marketDetailState.market?.currentPrice} $",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }

            QuadLineChart(data = marketDetailState.marketChart.prices)
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {},
        ) {
            FavoriteIcon(isFavorite = marketDetailState.market?.isFavorite ?: false) {
                onFavoriteClick(marketDetailState.market)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MarketDetailScreenPrev(
    @PreviewParameter(MarketDetailStateProvider::class)
    marketDetailState: MarketDetailContract.State,
) {
    ComposeNewsTheme {
        Surface {
            MarketDetailScreen(marketDetailState = marketDetailState, onFavoriteClick = {})
        }
    }
}
