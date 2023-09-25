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
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import ir.composenews.marketdetail.MarketDetailContract.State
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
    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
        market?.let {
            event.invoke(MarketDetailContract.Event.GetMarketDetail(marketId = market.id))
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
    marketDetailState: State,
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
            MarketData()
            MarketDetail(marketDetailState)
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

@Composable
private fun MarketDetail(marketDetailState: State) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MarketCap(marketDetailState)
        High24(marketDetailState)
        Low24(marketDetailState)
        Rank(marketDetailState)
    }
}

@Composable
private fun Rank(marketDetailState: State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Rank",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "#${marketDetailState.marketDetail.marketCapRank}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun Low24(marketDetailState: State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Low 24h",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = marketDetailState.marketDetail.marketData?.low24h?.usd.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun High24(marketDetailState: State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "High 24h",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = marketDetailState.marketDetail.marketData?.high24h?.usd.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun MarketCap(marketDetailState: State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Market Cap",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = formatNumber(
                marketDetailState.marketDetail.marketData?.marketCap?.usd,
            ),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun MarketData() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Market Data",
            style = MaterialTheme.typography.titleLarge,
        )
    }
    Divider(color = Color.Gray)
}

fun formatNumber(number: Long?): String {
    val format = number?.div(BILLION)
    if (format != null) {
        return if (format >= 1) {
            "$${format}B"
        } else {
            "$${format}M"
        }
    }
    return ""
}

@ThemePreviews
@Composable
private fun MarketDetailScreenPrev(
    @PreviewParameter(MarketDetailStateProvider::class) marketDetailState: State,
) {
    ComposeNewsTheme {
        Surface {
            MarketDetailScreen(marketDetailState = marketDetailState, onFavoriteClick = {})
        }
    }
}

private const val BILLION: Long = 1000000000L
