@file:Suppress("MagicNumber", "MaxLineLength", "LongMethod", "LongParameterList", "UnusedPrivateMember", "ktlint:standard:function-naming")

package ir.composenews.appwatch.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import ir.composenews.appwatch.ui.ComposeNewsWearTheme
import ir.composenews.appwatch.ui.WearTrendChange
import ir.composenews.appwatch.ui.wearScrollIndicator
import ir.composenews.base.LoadableData
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.MarketLogo
import ir.composenews.designsystem.component.Sparkline
import ir.composenews.designsystem.component.formatCompactUsd
import ir.composenews.designsystem.component.formatPrice
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.designsystem.theme.LocalExtendedColors
import ir.composenews.designsystem.theme.groupedRowShape
import ir.composenews.marketdetail.MarketDetailContract
import ir.composenews.marketdetail.MarketDetailContract.State
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.uimarket.model.MarketModel
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

private const val SPARKLINE_FILL_ALPHA = 0.35f

@Composable
fun MarketDetailWearRoute(market: MarketModel) {
    val (state, event) = use(viewModel = koinViewModel<MarketDetailViewModel>())
    LaunchedEffect(key1 = market) {
        event(MarketDetailContract.Event.SetMarket(market = market))
        event(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        event(MarketDetailContract.Event.GetMarketDetail(marketId = market.id))
    }
    MarketDetailScreen(
        market = state.market.data ?: market,
        state = state,
        onFavoriteClick = { event(MarketDetailContract.Event.OnFavoriteClick(market = it)) },
    )
}

/** The market on top, Market Data below it, and `Saved` as the edge button. */
@Composable
private fun MarketDetailScreen(
    market: MarketModel,
    state: State,
    onFavoriteClick: (MarketModel) -> Unit,
) {
    val columnState = rememberTransformingLazyColumnState()
    ScreenScaffold(
        scrollState = columnState,
        scrollIndicator = wearScrollIndicator(columnState),
        edgeButton = {
            SavedEdgeButton(
                isFavorite = market.isFavorite,
                onClick = { onFavoriteClick(market) },
                modifier =
                    Modifier.scrollable(
                        columnState,
                        orientation = Orientation.Vertical,
                        reverseDirection = true,
                        overscrollEffect = rememberOverscrollEffect(),
                    ),
            )
        },
    ) { contentPadding ->
        TransformingLazyColumn(
            state = columnState,
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item { MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 40.dp, shared = false) }
            item {
                Text(
                    text = market.symbol.uppercase(Locale.US),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            item { Text(text = formatUsdPrice(market.currentPrice), style = MaterialTheme.typography.titleLarge) }
            item { WearTrendChange(change = market.priceChangePercentage24h) }
            item { DayChart(state = state) }
            item {
                ListHeader(contentColor = MaterialTheme.colorScheme.tertiary) {
                    Text(text = "Market Data")
                }
            }
            val data = state.marketDetail.data?.marketData
            val rows =
                listOf(
                    "Market Cap" to data?.marketCapUSD?.let(::formatCompactUsd),
                    "High 24h" to data?.high24hUSD?.let(::formatPrice),
                    "Low 24h" to data?.low24hUSD?.let(::formatPrice),
                    "Rank" to (market.marketCapRank ?: state.marketDetail.data?.marketCapRank)?.let { "#$it" },
                )
            itemsIndexed(rows) { index, (label, value) ->
                MarketDataRow(label = label, value = value ?: "—", index = index, count = rows.size)
            }
        }
    }
}

/** The 1D price line, a loader while it loads, nothing if it failed (the price above still stands). */
@Composable
private fun DayChart(state: State) {
    Box(modifier = Modifier.fillMaxWidth().height(34.dp).padding(horizontal = 24.dp), contentAlignment = Alignment.Center) {
        when (val chart = state.marketChart) {
            is LoadableData.Loaded -> {
                Sparkline(
                    points = chart.data.prices.map { it.second },
                    color = LocalExtendedColors.current.chartLine,
                    strokeWidth = 2.dp,
                    fillAlpha = SPARKLINE_FILL_ALPHA,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            LoadableData.Loading, LoadableData.Initial -> {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .semantics { contentDescription = "Loading chart" },
                )
            }

            is LoadableData.Error -> {
                Unit
            }
        }
    }
}

/** One row of the grouped Market Data list; Market Cap leads it on primaryContainer. */
@Composable
private fun MarketDataRow(
    label: String,
    value: String,
    index: Int,
    count: Int,
) {
    val lead = index == 0
    val colors = MaterialTheme.colorScheme
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
                .clip(groupedRowShape(index, count))
                .background(if (lead) colors.primaryContainer else colors.surfaceContainer)
                .padding(horizontal = 14.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (lead) colors.onPrimaryContainer else colors.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (lead) colors.onPrimaryContainer else colors.onSurface,
            maxLines = 1,
        )
    }
}

/** `Saved` on primary once saved, `Favorite` on primaryContainer before; read out like the phone's toggle. */
@Composable
private fun SavedEdgeButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    EdgeButton(
        onClick = onClick,
        colors =
            if (isFavorite) {
                ButtonDefaults.buttonColors()
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = colors.primaryContainer,
                    contentColor = colors.onPrimaryContainer,
                    iconColor = colors.onPrimaryContainer,
                )
            },
        modifier =
            modifier.semantics {
                stateDescription = if (isFavorite) "In favorites" else "Not in favorites"
                onClick(label = if (isFavorite) "Remove from favorites" else "Add to favorites", action = null)
            },
    ) {
        Icon(
            painter = painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = if (isFavorite) "Saved" else "Favorite")
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
private fun MarketDetailScreenPreview() {
    val market = MarketModel("bitcoin", "Bitcoin", "btc", 67_412.80, 2.14, "", isFavorite = true, marketCapRank = 1)
    ComposeNewsWearTheme {
        AppScaffold {
            MarketDetailScreen(market = market, state = State(market = LoadableData.Loaded(market)), onFavoriteClick = {})
        }
    }
}
