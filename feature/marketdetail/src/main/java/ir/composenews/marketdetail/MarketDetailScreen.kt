@file:Suppress(
    "UnusedPrivateMember",
    "LongMethod",
    "ForbiddenComment",
    "MagicNumber",
    "ktlint"
)

package ir.composenews.marketdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import ir.composenews.base.LoadableComponent
import ir.composenews.base.LoadableData
import ir.composenews.base.use
import ir.composenews.designsystem.component.FavoriteIcon
import ir.composenews.designsystem.component.shimmerEffect
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.marketdetail.MarketDetailContract.State
import ir.composenews.marketdetail.components.MarketData
import ir.composenews.marketdetail.components.QuadLineChart
import ir.composenews.marketdetail.preview_provider.MarketDetailStateProvider
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MarketDetailRoute(
    market: MarketModel,
    viewModel: MarketDetailViewModel = hiltViewModel(),
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
        // TODO: Move into viewModel?
        event.invoke(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        event.invoke(MarketDetailContract.Event.GetMarketDetail(marketId = market.id))
    }

    MarketDetailScreen(
        marketDetailState = state,
        onFavoriteClick = {
            event.invoke(MarketDetailContract.Event.OnFavoriteClick(market = it))
        },
    )
}

@Composable
internal fun MarketDetailScreen(
    modifier: Modifier = Modifier,
    marketDetailState: State,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
        ) {
            TopAppBar(
                market = marketDetailState.market,
            )
            QuadLineChart(
                loadableData = marketDetailState.marketChart,
            )
            MarketData(
                loadableData = marketDetailState.marketDetail,
            )
        }
        MarketDetailFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            loadableData = marketDetailState.market,
            onFavoriteClick = onFavoriteClick,
        )
    }
}

@Composable
private fun MarketDetailFloatingActionButton(
    modifier: Modifier = Modifier,
    loadableData: LoadableData<MarketModel>,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    LoadableComponent(
        loadableData = loadableData,
        loading = {},
        loaded = { data ->
            FloatingActionButton(
                modifier = modifier,
                onClick = { onFavoriteClick(data) },
            ) {
                FavoriteIcon(isFavorite = data.isFavorite)
            }
        },
        error = { error -> },
    )
}

@Composable
private fun TopAppBar(
    market: LoadableData<MarketModel>,
) {
    LoadableComponent(
        loadableData = market,
        loaded = { data ->
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
                        painter = rememberAsyncImagePainter(model = data.imageUrl),
                        contentDescription = data.name,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                    )
                    Column(
                        modifier = Modifier.weight(1F),
                    ) {
                        Text(
                            text = data.name,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = "${data.currentPrice} $",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        },
        loading = {
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
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .shimmerEffect(),
                    )
                    Column(
                        modifier = Modifier.weight(1F),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(20.dp)
                                .shimmerEffect(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(12.dp)
                                .shimmerEffect(),
                        )
                    }
                }
            }
        },
        error = {
        },
    )
}

// TODO: Move from UI Layer to domain

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
        MarketDetailScreen(
            marketDetailState = marketDetailState,
            onFavoriteClick = {},
        )
    }
}

private const val BILLION: Long = 1000000000L
