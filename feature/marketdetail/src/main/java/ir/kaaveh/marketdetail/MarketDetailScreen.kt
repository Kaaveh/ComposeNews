package ir.kaaveh.marketdetail

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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import ir.kaaveh.base.BaseViewModel
import ir.kaaveh.designsystem.component.FavoriteIcon
import ir.kaaveh.designsystem.component.QuadLineChart
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.theme.ComposeNewsTheme
import ir.kaaveh.base.use
import ir.kaaveh.domain.model.Market
import ir.kaaveh.marketdetail.preview_provider.MarketDetailStateProvider

@Composable
fun MarketDetailRoute(
    market: Market?,
    viewModel: MarketDetailViewModel = hiltViewModel(),
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
        market?.let {
            event.invoke(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        }
    }

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
    }

    MarketDetailScreen(
        marketDetailState = state,
        onFavoriteClick = {
            event.invoke(MarketDetailContract.Event.OnFavoriteClick(market = it))
        },
    )
}

@Composable
private fun MarketDetailScreen(
    marketDetailState: MarketDetailContract.State,
    onFavoriteClick: (market: Market?) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = marketDetailState.market?.imageUrl),
                        contentDescription = marketDetailState.market?.name,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = marketDetailState.market?.name ?: "--",
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = "${marketDetailState.market?.currentPrice} $",
                            style = MaterialTheme.typography.body1
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
            backgroundColor = Color.White,
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
    marketDetailState: MarketDetailContract.State
) {
    ComposeNewsTheme {
        Surface {
            MarketDetailScreen(marketDetailState = marketDetailState, onFavoriteClick = {})
        }
    }
}